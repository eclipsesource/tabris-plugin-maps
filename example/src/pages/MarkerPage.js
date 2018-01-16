const {Composite, Page, TextView} = require('tabris');

class MarkerPage extends Page {

  constructor() {
    super({title: MarkerPage.NAME});
    this._createUI();
  }

  static get NAME() {
    return 'Marker';
  }

  _createUI() {
    let controls = new Composite({
      left: 0, right: 0, bottom: 0, height: 64,
      background: 'white',
      elevation: 6
    }).appendTo(this);

    this._map = new esmaps.Map({
      left: 0, right: 0, top: 0, bottom: controls
    }).on('ready', ({target: map}) => {
      map.region = {southWest: [48.812059, 2.2495793], northEast: [48.910537, 2.4205543]};
      this._createMarker([48.8560453, 2.3415578], 'Notre-dame');
      this._createMarker([48.8566333, 2.2975081], 'Eifel tower', {src: 'images/eiffel-tower.png', scale: 2});
    }).appendTo(this);

    this._statusLabel = new TextView({
      left: 16, right: 16, top: 16, bottom: 16,
      markupEnabled: true,
      text: 'Tap on marker...'
    }).appendTo(controls);
  }

  _createMarker(position, title, image) {
    let marker = new esmaps.Marker({position: position, image})
      .on('tap', () => this._statusLabel.text = 'Tapped on <b>' + title + '</b>');
    this._map.addMarker(marker);
  }

}

module.exports = MarkerPage;
