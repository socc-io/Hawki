import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import Scroll, { Element } from 'react-scroll';
import './App.css';
import { defer } from 'q';
// import _ from 'lodash';
import daumWrapper from './daumAPI';
import BuildingInfo from './BuildingInfo';

import { Jumbotron, Button,
Form, FormGroup, FormControl, ControlLabel } from 'react-bootstrap';

const introMesssage = [
  'Hawki is the framework system for indoor positioning service.',
  'Indoor positioning technology will use in a variety of ways including IOT, Indoor-navigation.',
  'Hawki allows you to find where you are in the building or subway by using your wifi-enabled device such as android, iphone, etc',
  'You can register map of building in this page.',
];

class App extends Component {
  constructor(props) {
    super(props);

    this.APIKey = '1805998ae6237e2bd8f05f5aab8965f8';
    this.daumAPILoadPromise = daumWrapper.load(this.APIKey);

    this.initDeffered = defer();
    this.initPromise = this.initDeffered.promise;

    this.state = {
      mapImage: '',
      options: {},
      position: [33.450701, 126.570667],
      buildingName: '강남역',
      searchResults: [],
      markers: [],
      selected: null,
      uploaderMessage: '',
    }
  }
  componentDidMount() {
    const containerDiv = ReactDOM.findDOMNode(this.refs.daumMapContainer);
    // ReactDOM.render(this.props.daumAPILoading, containerDiv);
    this.daumAPILoadPromise.then(() => {
      ReactDOM.unmountComponentAtNode(containerDiv);
      const daumAPI = daumWrapper.getDaumMapAPI();
      const options = {
        center: new daumAPI.LatLng(...this.state.position),
        level: 3,
        scrollwheel: false,
      };
      const daumMap = new daumAPI.Map(containerDiv, options);

      // daumAPI.event.addListener(daumMap, 'center_changed', this.handleMove);
      // daumAPI.event.addListener(daumMap, 'bounds_changed', this.handleBoundsChange);
      // daumAPI.event.addListener(daumMap, 'zoom_changed', this.handleZoomChange);

      const clusterer = new daumAPI.MarkerClusterer({
        map: daumMap,
        markers: this.state.markers,
        gridSize: 35,
        averageCenter: true,
        minLevel: 6,
        disableClickZoom: true,
        styles: [{
          width: '53px', height: '52px',
          color: '#fff',
          textAlign: 'center',
          lineHeight: '54px',
        }],
      });

      this.map = daumMap;
      this.mapAPI = daumWrapper.getDaumMapAPI();
      this.clusterer = clusterer;
      this.places = new this.mapAPI.services.Places();
      this.setState({ options });
      this.initDeffered.resolve();
    })
    .catch((rejection) => {
      console.error(rejection);
      ReactDOM.unmountComponentAtNode(containerDiv);
      // ReactDOM.render(this.props.daumAPILoadFailed, containerDiv);
      this.initDeffered.reject(rejection);
    });
  }
  zoom(diff) {
    const level = this.map.getLevel();
    this.map.setLevel(level + diff);
  }
  handleBuildingNameChange(event) {
    const name = event.target.value
    this.setState({ buildingName: name });
    // this.updateSearchResults(name);
  }
  handleBuildingFocus(building) {
    this.setState({ buildingName: building.place_name,
      searchResults: [building],
    })
  }
  updateSearchResults(event) {

    if(event) event.preventDefault();

    const name= this.state.buildingName;
    if(this.places && this.mapAPI && this.clusterer) {
      this.places.keywordSearch(name, (result, status) => {
        if(status === this.mapAPI.services.Status.OK) {
          console.log('searched', result);
          this.clusterer.clear();

          const newMarkers = result.map(e => (new this.mapAPI.Marker({
            position: new this.mapAPI.LatLng(e.y, e.x),
            title: e.place_name,
            clickable: true,
            range: 999999999999999999,
          })))

          result.forEach((e,idx) => {
            this.mapAPI.event.addListener(newMarkers[idx], 'click', () => {
              console.log('clicked', e);
              this.setState({ selected: e });
              Scroll.scroller.scrollTo('BuildingInfo-'+idx, {
                duration: 300,
                smooth: true,
              });
            })
          })

          this.clusterer.addMarkers(newMarkers);
          this.setState({ searchResults: result });
          this.map.setCenter(new this.mapAPI.LatLng(result[0].y, result[0].x));
        }
      })
    }
  }
  render() {
    return (
      <div className="App">
        <Jumbotron>
          <h1>Hawk-I</h1>
          {introMesssage.map((msg, idx) => (
            <p key={idx}>{msg}</p>
          ))}
          <p><Button href='https://github.com/socc-io/hawki' bsStyle="primary">Go Github Page</Button></p>
        </Jumbotron>
        <Form inline onSubmit={(e) => this.updateSearchResults(e)}>
          <FormGroup controlId="formInlineName">
            <ControlLabel>Building Name</ControlLabel>
            {' '}
            <FormControl type="text" placeholder="대한민국 주소" value={this.state.buildingName}
              onChange={(e) => this.handleBuildingNameChange(e)}
              onSubmit={() => this.updateSearchResults()}
            />
          </FormGroup>
          {' '}
          <Button onClick={(e) => this.updateSearchResults(e)}>
            Search Building
          </Button>
        </Form>
        <div ref='daumMapContainer' className="Daum-map" ></div>
        <Button onClick={() => this.zoom(1)}>-</Button>
        <Button onClick={() => this.zoom(-1)}>+</Button>
        <div className="search-List">
          {this.state.searchResults.map((result, idx) => (
            <Element key={idx} name={'BuildingInfo-' + idx}>
              <BuildingInfo focus={() => this.handleBuildingFocus(result)} info={result} />
            </Element>
          ))}
        </div>
        <div className="footer"></div>
      </div>
    );
  }
}

export default App;
