/**=========================================================
 * Module: clear-storage.js
 * Removes a key from the browser storage via element click
 =========================================================*/

(function($, window, document){
  'use strict';

  if( !store || !store.enabled ) return;

  var Selector = '[data-toggle="reset"]';

  $(document).on('click', Selector, function (e) {
      e.preventDefault();
      var key = $(this).data('key');

      if(key) {
        store.remove(key);
      }
      else {
        var shouldClear = confirm("This action will restore the current app state.");
        if (shouldClear == true) {
          store.clear();
        }
      }

      // at last, reload the page
      window.location.reload();

  });

}(jQuery, window, document));
