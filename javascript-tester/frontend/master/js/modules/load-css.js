/**=========================================================
 * Module: load-css.js
 * Request and load into the current page a css file
 =========================================================*/

(function($, window, document){
  'use strict';

  var Selector = '[data-toggle="load-css"]',
      storageKeyName = 'autoloadCSS';

  restoreStylesheet();
  $(document)
    .ready(function() {
    })
    .on('click', Selector, function (e) {
      e.preventDefault();
      var uri = $(this).data('uri');

      createStylesheet(uri);

  });

  // Creates a link element and injects the stylesheet href
  function createStylesheet(uri) {
    var link;
    if(uri) {
      link = createLink();
      if(link) {
        injectStylesheet(link, uri);
      }
      else {
        $.error('Error creating new stylsheet link element.');
      }
    }
    else {
      $.error('No stylesheet location defined.');
    }
  }

  function createLink() {
    var linkId = 'autoloaded-stylesheet',
        link = $('#'+linkId);

    if( ! link.length ) {
      var newLink = $('<link rel="stylesheet">').attr('id', linkId);
      $('head').append(newLink);
      link = $('#'+linkId);
    }
    return link;
  }

  function injectStylesheet(element, uri) {
    var v = '?id='+Math.round(Math.random()*10000); // forces to jump cache
    if(element.length) {
      element.attr('href', uri + v);
    }
    saveStylesheet(uri);
  }

  // Save the loaded stylesheet link
  function saveStylesheet(uri) {
    store.set(storageKeyName, uri);
  }
  // Restores the stylesheet 
  function restoreStylesheet() {
    var uri = store.get(storageKeyName);
    if(uri) {
      createStylesheet(uri);
    }
  }

}(jQuery, window, document));
