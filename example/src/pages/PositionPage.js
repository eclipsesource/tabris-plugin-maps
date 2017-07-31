const {Button, Composite, Page, TextView} = require('tabris');

class PositionPage extends Page {

  constructor() {
    super({title: PositionPage.name});
    this._createUI();
  }

  static get name() {
    return 'Position';
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
      map.position = [48.8644458, 2.3589976];
      this._updatePositionLabel();
    }).appendTo(this);

    let paris = new Button({
      left: 16, right: ['50%', 8], top: 16,
      text: 'Show Paris'
    }).on('select', () => this._map.position = [48.8644458, 2.3589976])
      .appendTo(controls);

    new Button({
      left: ['50%', 8], right: 16, top: 16,
      text: 'Show Sydney'
    }).on('select', () => this._map.position = [-33.867, 151.206])
      .appendTo(controls);

    let updatePositionButton = new Button({
      text: 'Get position',
      left: 16, top: [paris, 16], bottom: 16
    }).on('select', () => this._updatePositionLabel())
      .appendTo(controls);

    this._positionLabel = new TextView({
      left: [updatePositionButton, 16], right: 16, top: [paris, 16], bottom: 16,
      markupEnabled: true
    }).appendTo(controls);
  }

  _updatePositionLabel() {
    let position = this._map.position.map(coordinate => coordinate.toFixed(6));
    this._positionLabel.text =
      `Latitude: <b>${position[0]}</b><br/>` +
      `Longitude: <b>${position[1]}</b>`;
  }

}

module.exports = PositionPage;
