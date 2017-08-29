const {Button, NavigationView, Page, ui} = require('tabris');
const PositionPage = require('./pages/PositionPage');
const CameraPage = require('./pages/CameraPage');
const RegionPage = require('./pages/RegionPage');
const MarkerPage = require('./pages/MarkerPage');

const PAGES = [PositionPage, CameraPage, RegionPage, MarkerPage];

let navigationView = new NavigationView({
  left: 0, top: 0, right: 0, bottom: 0
}).appendTo(ui.contentView);

let mainPage = new Page({title: 'Maps examples'}).appendTo(navigationView);

PAGES.forEach(createPageButton);

function createPageButton(PageConstructor) {
  new Button({
    left: 16, top: 'prev() 16', right: 16,
    text: 'Show \'' + PageConstructor.NAME.toLowerCase() + '\' example'
  }).on('select', () => new PageConstructor().appendTo(navigationView))
    .appendTo(mainPage);
}
