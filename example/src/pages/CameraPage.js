const {Button, Composite, Page, TextView} = require('tabris');

class CameraPage extends Page {

  constructor() {
    super({title: CameraPage.name});
    this._createUI();
  }

  static get name() {
    return 'Camera';
  }

  _createUI() {
    let controls = new Composite({
      left: 0, right: 0, bottom: 0,
      background: 'white',
      elevation: 6
    }).appendTo(this);

    this._map = new esmaps.Map({
      left: 0, right: 0, top: 0, bottom: controls
    }).on('ready', ({target: map}) => {
      map.on('cameraChanged', () => this._updateStatus('Camera changed programmatic'))
        .on('cameraMoved', () => this._updateStatus('Camera changed by user'));
      map.camera = {position: [48.8644458, 2.3589976]};
    }).appendTo(this);

    let paris = new Button({
      left: 16, right: '50% 8', top: 16,
      text: 'Show Paris'
    }).on('select', () => this._map.camera = {position: [48.8644458, 2.3589976]})
      .appendTo(controls);

    new Button({
      left: '50% 8', right: 16, top: 16,
      text: 'Show Sydney'
    }).on('select', () => this._map.camera = {position: [-33.867, 151.206]})
      .appendTo(controls);

    this._statusLabel = new TextView({
      id: 'label',
      left: 16, right: 16, top: [paris, 16], bottom: 16,
      lineSpacing: 1.2,
      markupEnabled: true
    }).appendTo(controls);
  }

  _updateStatus(message) {
    let position = this._map.camera.position.map(coordinate => coordinate.toFixed(6));
    this._statusLabel.text =
      `<b>${message}</b><br/>` +
      `Position: [ ${position[0]}, ${position[1]} ]`;
  }

}

module.exports = CameraPage;
