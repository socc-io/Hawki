import React from 'react';
import ReactDOM from 'react-dom';

import { Layer, Stage, Image, Circle } from 'react-konva';

import './BuildingInfo.css';

import {
  Modal,
  Button,
  Form,
  FormGroup,
  Col,
  ControlLabel,
  FormControl,
  Table,
} from 'react-bootstrap';

// const APIBase = 'http://localhost:4000';
const APIBase = 'http://hawki.smilu.link:4000';

export default class BuildingInfo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      newImage: null,
      mapImage: null,
      scale: 1.0,
      imageExist: false,
      uploadModal: false,
      mapImageSrc: 'https://via.placeholder.com/1200x500',
      clickPos: null,
      POIName: '',
      POIUrl: '',
      pois: [],
    }
    this.imageWidth = 700;
    this.imageHeight = 500;
    this.imageNotExistImage = `https://via.placeholder.com/${this.imageWidth}x${this.imageHeight}`;
  }
  componentDidMount() {
    // this.imageRect = ReactDOM.findDOMNode(this.refs.mapImage)
    //   .getBoundingClientRect();

    this.id = this.props.info.id;
    this.imageURL = APIBase + '/static/map/' + this.id + '.jpg';
    this.updateMapImage();

    this.updatePOI();
  }
  componentDidUpdate() {
    // this.imageRect = ReactDOM.findDOMNode(this.refs.mapImage).getBoundingClientRect();
  }
  componentWillReceiveProps(nextProps) {
    if(nextProps.info.id !== this.props.info.id) {
      console.log('updated', nextProps.info.id);
      this.id = nextProps.info.id;
      this.imageURL = APIBase + '/static/map/' + this.id + '.jpg';
      this.updateMapImage();

      this.updatePOI();

      this.setState({ clickPos: null })
    }
  }
  updateMapImage() {
    const image = new window.Image();
    image.onload = () => {
      this.setState({ mapImage: image });
    }
    fetch(APIBase + `/building/${this.id}/mapimage`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    })
    .then((res) => res.json())
    .then((res) => {
      if(res.success === 1) {
        image.src = APIBase + `/static/map/${this.id}.jpg?t=${new Date().getTime()}`;
        this.setState({ loading: false, imageExist: true});
      } else {
        image.src = this.imageNotExistImage;
        this.setState({ loading: false, imageExist: false });
      }
    })
    .catch((e) => {
      image.src = this.imageNotExistImage;
      this.setState({ loading: false, imageExist: false });
    })
  }
  updatePOI() {
    fetch(APIBase + `/building/${this.id}/poi`)
    .then(res => res.json())
    .then(res => {
      if(res.success) {
        console.log('receive pois', res.pois);
        this.setState({ pois: res.pois });
      }
    })
  }
  clickImage(event) {
    if(this.state.imageExist) {
      const { scale } = this.state;
      const { layerX, layerY } = event.evt;
      const { x, y } = this.refs.imageLayer.attrs;

      const rx = (layerX / scale) - x;
      const ry = (layerY / scale) - y;

      console.log('clicked', rx, ry);
      this.setState({ clickPos: [rx, ry] });
    }
  }
  handleImageSelected(event) {
    if(event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.setState({ newImage: e.target.result, uploadModal: true });
      }
      reader.readAsDataURL(file);
    }
    event.target.value = '';
  }
  deleteNewImage() {
    this.setState({ modal: null })
  }
  uploadNewImage() {
    // this.setState({ mapImageSrc: this.state.newImage });

    const body = JSON.stringify({
      image: this.state.newImage,
    });

    fetch(APIBase + `/building/${this.id}/mapimage`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body,
    })
    .then((res) => res.json())
    .then((res) => {
      if(res.success) {
        console.log('success to upload');
        this.updateMapImage();
        this.setState({
          uploaderMessage: 'successfully uploaded ' + this.state.selected.place_name,
          uploadModal: false,
        });
      } else {
        this.setState({ uploaderMessage: 'failed to upload', uploadModal: false });
      }
    })
    .catch(e => {
      this.setState({ uploaderMessage: 'failed to upload', uploadModal: false });
    })
  }
  clearAllPOI() {
    fetch(APIBase + `/building/${this.id}/poi/all`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    })
    .then(res => res.json())
    .then(res => {
      if(res.success) {
        this.updatePOI();
        this.setState({ clickedPOI: null });
        alert('successfully deleted all pois');
      } else {
        alert('failed to delete all pois');
      }
    })
    .catch(e => {
      alert('failed to delete all pois');
    })
  }
  deletePOI(poi) {
    fetch(APIBase + `/building/${this.id}/poi/${poi.id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
    })
    .then(res => res.json())
    .then(res => {
      if(res.success) {
        this.updatePOI();
        this.setState({ clickedPOI: null })
        alert('successfully deleted POI: ' + poi.name);
      } else {
        alert('failed to delete POI');
      }
    })
    .catch(e => {
      alert('failed to delete POI');
    })
  }
  registerPOI() {
    const name = this.state.POIName;
    const url = this.state.POIUrl;
    const [x, y] = this.state.clickPos;
    if(name.length === 0) {
      alert('Please input name');
      return;
    }
    fetch(APIBase + `/building/${this.id}/poi`, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ name, url, x, y }),
    })
    .then(res => res.json())
    .then(res => {
      if(res.success) {
        this.updatePOI();
        alert('successfully registered POI: ' + name);
      } else {
        alert('failed to register POI');
      }
    })
    .catch(e => {
      alert('failed to register POI');
    })
    this.setState({ clickPos: null });
  }
  cancelPOI() {
    this.setState({ clickPos: null });
  }
  handlePOIClicked(poi, idx) {
    console.log('poi clicked', poi);
    this.setState({ clickPos: null, clickedPOI: poi });
  }
  getImageAbsolutePosition(x, y) {
    return {
      left: x - 5,
      top: y - this.imageRect.height - 5,
    };
  }
  renderBuildingDetail() {
    return (
      <div>
        <Table striped bordered condensed hover>
          <thead>
            <tr>
              <th>name</th>
              <th>value</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>주소</td>
              <td>{this.props.info.address_name}</td>
            </tr>
            <tr>
              <td>도로명주소</td>
              <td>{this.props.info.road_address_name}</td>
            </tr>
            <tr>
              <td>식별번호</td>
              <td>{this.props.info.id}</td>
            </tr>
          </tbody>
        </Table>
      </div>
    )
  }
  renderPOIDetail() {
    const poi = this.state.clickedPOI;
    return (
      <div>
        <Table striped bordered condensed hover>
          <thead>
            <tr>
              <th>name</th>
              <th>value</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>name</td>
              <td>{poi.name}</td>
            </tr>
            <tr>
              <td>url</td>
              <td>{poi.url}</td>
            </tr>
            <tr>
              <td>x</td>
              <td>{poi.x}</td>
            </tr>
            <tr>
              <td>y</td>
              <td>{poi.y}</td>
            </tr>
          </tbody>
        </Table>
        <Button onClick={() => this.deletePOI(poi)}>Delete</Button>
      </div>
    )
  }
  renderPOIForm() {
    return (
      <Form horizontal>
        <FormGroup controlId="formHorizontalEmail">
          <Col componentClass={ControlLabel} sm={2}>
            Name
          </Col>
          <Col sm={10}>
            <FormControl type="text" placeholder="Name" value={this.state.POIName}
              onChange={(e) => this.setState({ POIName: e.target.value })}
            />
          </Col>
        </FormGroup>

        <FormGroup controlId="formHorizontalPassword">
          <Col componentClass={ControlLabel} sm={2}>
            URL
          </Col>
          <Col sm={10}>
            <FormControl type="text" placeholder="URL" value={this.state.POIUrl}
              onChange={(e) => this.setState({ POIUrl: e.target.value })}
            />
          </Col>
        </FormGroup>

        <FormGroup>
          <Col smOffset={2} sm={10}>
            <Button onClick={() => this.registerPOI()} bsStyle="primary">
              Register
            </Button>
            <Button onClick={() => this.cancelPOI()}>
              Cancel
            </Button>
          </Col>
        </FormGroup>
      </Form>
    )
  }
  renderImage() {
    return (
      <div>
        <Stage width={this.imageWidth} height={this.imageHeight}
          scaleX={this.state.scale}
          scaleY={this.state.scale}
        >
          <Layer
            ref='imageLayer'
            draggable={this.state.imageExist}>
            <Image
              image={this.state.mapImage}
              onclick={(e) => this.clickImage(e)}/>
            {this.state.clickPos ? (
              <Circle
                radius={10}
                x={this.state.clickPos[0]}
                y={this.state.clickPos[1]}
                fill='red'/>
            ):null}
            {this.state.pois.map((poi, idx) => (
              <Circle
                key={idx}
                onclick={() => this.handlePOIClicked(poi, idx)}
                radius={10}
                x={poi.x}
                y={poi.y}
                fill='blue'/>
            ))}
          </Layer>
        </Stage>
        <Button onClick={() => this.setState({ scale: this.state.scale - 0.1 })}>-</Button>
        <Button onClick={() => this.setState({ scale: this.state.scale + 0.1 })}>+</Button>
      </div>
    )
  }
  render() {
    return (
      <div className='Building-info-container'>
        <h1>{this.props.info.place_name}</h1>
        {this.renderImage()}
        <input type='file' ref='imageSelector' accept="image/*" style={{display: 'none'}} onChange={(e) => this.handleImageSelected(e)}/>
        <div className='Building-info-detail-container'>
          {this.renderBuildingDetail()}
          <Button href={this.props.info.place_url} bsStyle="primary">Daum Map</Button>
          <Button onClick={() => this.refs.imageSelector.click()}>Upload Image</Button>
          <Button onClick={() => this.clearAllPOI()}>Clear All POI</Button>
          <Button onClick={() => this.props.focus()}>Focus</Button>
        </div>
        {this.state.clickPos ? this.renderPOIForm() :  null}
        {this.state.clickedPOI ? this.renderPOIDetail() : null}
        <div className="static-modal" style={{ display: (this.state.uploadModal ? 'block' : 'none') }}>
          <Modal.Dialog>
            <Modal.Header>
              <Modal.Title>Update Building Map Image</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              Do you really want to upload image to server?
              It may overwrite current image
            </Modal.Body>
            <Modal.Footer>
              <Button onClick={() => this.deleteNewImage()}>Close</Button>
              <Button bsStyle="primary" onClick={() => this.uploadNewImage()}>Upload image</Button>
            </Modal.Footer>

          </Modal.Dialog>
        </div>
      </div>
    )
  }
};
