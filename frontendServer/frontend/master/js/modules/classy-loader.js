/**=========================================================
 * Module: classy-loader.js
 * Enable use of classyloader directly from data attributes
 =========================================================*/

(function($, window, document){
  'use strict';

  var Selector        = '[data-toggle="classyloader"]',
      $scroller       = $(window),
      inViewFlagClass = 'js-is-in-view'; // a classname to detect when a chart has been triggered after scroll

  $(Selector).each(function (e) {

      var $element = $(this),
          options  = $element.data();

      // At lease we need a data-percentage attribute
      if(options) {
        if( options.triggerInView ) {

          $scroller.scroll(function() {
            var offset = 0;
            if( ! $element.hasClass(inViewFlagClass) &&
                $.Utils.isInView($element, {topoffset: offset}) ) {
              $element.ClassyLoader(options).addClass(inViewFlagClass);
            }
          });

        }
        else
          $element.ClassyLoader(options);
      }

  });

}(jQuery, window, document));
