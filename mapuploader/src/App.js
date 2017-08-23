import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './App.css';
import { defer } from 'q';
// import _ from 'lodash';
import daumWrapper from './daumAPI';


// const APIBase = 'http://hawki.smilu.link';
const APIBase = 'http://localhost:4000';

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
      buildingName: '명달로6길 20',
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
      const daumLatLng = new daumAPI.LatLng(...this.state.position);
      const options = {
        center: daumLatLng,
        level: 3,
      };
      const daumMap = new daumAPI.Map(containerDiv, options);

      daumAPI.event.addListener(daumMap, 'center_changed', this.handleMove);
      daumAPI.event.addListener(daumMap, 'bounds_changed', this.handleBoundsChange);
      daumAPI.event.addListener(daumMap, 'zoom_changed', this.handleZoomChange);

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
  upload() {
    if(this.state.mapImage.length <= 0) {
      alert('Please select map image file');
      return;
    }
    if(this.state.selected === null) {
      alert('Please select building to submit');
      return;
    }
    const body = JSON.stringify({
      image: this.state.mapImage,
      id: this.state.selected.id,
    });

    fetch(APIBase + '/poi', {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body,
    })
    .then((res) => res.json())
    .then((res) => {
      if(res.success === 1) {
        this.setState({ uploaderMessage: 'successfully uploaded ' + this.state.selected.place_name });
      } else {
        this.setState({ uploaderMessage: 'failed to upload' });
      }
    })
    .catch(e => {
      this.setState({ uploaderMessage: 'failed to upload' });
    })
  }
  handleMove() {

  }
  handleBoundsChange() {

  }
  handleZoomChange() {

  }
  onMapImageChange(event) {
    if(event.target.files && event.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.setState({ mapImage: e.target.result });
      }
      reader.readAsDataURL(event.target.files[0]);
    }
  }
  handleBuildingNameChange(event) {
    const name = event.target.value
    this.setState({ buildingName: name });
    // this.updateSearchResults(name);
  }
  updateSearchResults(name) {
    if(name === undefined) {
      name = this.state.buildingName;
    }
    if(this.places && this.mapAPI && this.clusterer) {
      this.places.keywordSearch(name, (result, status) => {
        if(status === this.mapAPI.services.Status.OK) {
          console.log('searched', result);
          this.clusterer.clear();

          const newMarkers = result.map(e => (new this.mapAPI.Marker({
            position: new this.mapAPI.LatLng(e.y, e.x),
            title: e.place_name,
            clickable: true,
          })))

          result.forEach((e,idx) => {
            this.mapAPI.event.addListener(newMarkers[idx], 'click', () => {
              console.log('clicked', e);
              this.setState({ selected: e });
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
        <div className="App-header">
          <h1><a href="https://github.com/socc-io/hawki">HawkI POI Page</a></h1>
        </div>
        <div className="App-intro">
          <p>Hawki is the framework system for indoor positioning service.</p>
          <p>Indoor positioning technology will use in a variety of ways including IOT, Indoor-navigation.</p>
          <p>Hawki allows you to find where you are in the building or subway by using your wifi-enabled device such as android, iphone, etc.</p>
          <p>You can register map of building in this page.</p>
          <p>{this.state.searchResults.length} buildings found</p>
          {this.state.selected ? (
            <p>
              {this.state.selected.place_name}({this.state.selected.id}) is selected
            </p>
          ) : null}
          {this.state.uploaderMessage ? (
            <p>
              {this.state.uploaderMessage}
            </p>
          ) : null}
        </div>
        <div className="Building-form">
          <img alt="mapThumb" className="map-thumb" src={this.state.mapImage} /> <br/>
          <input type="file" name="mapImageInput" onChange={(e) => this.onMapImageChange(e)} accept="image/*" />
        </div>
        <div className="Building-map">
          <div className='search-div'>
            <input className='Building-name-input' type='text' name='buildingName' value={this.state.buildingName} onChange={(e) => this.handleBuildingNameChange(e)} />
            <input className="Building-name-submit" type="submit" value="SEARCH" onClick={() => this.updateSearchResults()} />
          </div>
          <div ref='daumMapContainer' className="Daum-map" style={{ width: '500px', height: '400px' }} ></div>
          <input className="Building-upload-submit" type="submit" value="UPLOAD"
            onClick={() => this.upload()} />
        </div>
        <div className="footer">
        </div>
      </div>
    );
  }
}

export default App;
