const {Button, Composite, Page, TextView} = require('tabris');

class RegionPage extends Page {

  constructor() {
    super({title: RegionPage.NAME});
    this._createUI();
  }

  static get NAME() {
    return 'Region';
  }

  _createUI() {
    let controls = new Composite({
      left: 0, right: 0, bottom: 0, height: 152,
      background: 'white',
      elevation: 6
    }).appendTo(this);

    this._map = new esmaps.Map({
      left: 0, right: 0, top: 0, bottom: controls
    }).on('ready', ({target: map}) => {
      map.on('cameraChanged', () => this._updateRegionLabel())
        .on('cameraMoved', () => this._updateRegionLabel());
      map.region = {southWest: [48.812059, 2.2495793], northEast: [48.910537, 2.4205543]};
    }).appendTo(this);

    let paris = new Button({
      left: 16, right: ['50%', 8], top: 16,
      text: 'Show Paris'
    }).on('select', () => {
      this._map.region = {southWest: [48.812059, 2.2495793], northEast: [48.910537, 2.4205543]};
    }).appendTo(controls);

    new Button({
      left: '50% 8', right: 16, top: 16,
      text: 'Show Sydney'
    }).on('select', () => {
      this._map.region = {southWest: [-33.912452, 151.1260233], northEast: [-33.785166, 151.2875383]};
    }).appendTo(controls);

    this._regionLabel = new TextView({
      left: 16, right: 16, bottom: 16,
      lineSpacing: 1.2,
      markupEnabled: true
    }).appendTo(controls);
  }

  _updateRegionLabel() {
    let southWest = this._map.region.southWest.map(coordinate => coordinate.toFixed(6));
    let northEast = this._map.region.northEast.map(coordinate => coordinate.toFixed(6));
    let text =
      '<b>Region</b><br/>' +
      `southWest: [ ${southWest[0]}, ${southWest[1]} ]<br/>` +
      `northEast: [ ${northEast[0]}, ${northEast[1]} ]`;
    this._regionLabel.set({text});
  }

}

module.exports = RegionPage;
