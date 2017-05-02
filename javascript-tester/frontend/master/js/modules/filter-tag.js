/**=========================================================
 * Module: filter-tag.js
 * Basic items filter 
 =========================================================*/

(function($, window, document){
  'use strict';

  var selectorFilterTag   = '[data-filter-tag]',    // the trigger button
      selectorFilterGroup = '[data-filter-group]',  // items to be filtered
      itemAnimation       = 'fadeIn';               // supported by animo.js

  $(function() {

      $(selectorFilterGroup).first().closest('.row').eq(0).css('overflow','hidden');

      $(document).on('click', selectorFilterTag, function() {

        var $this = $(this),
            targetGroup = $this.data('filterTag');


        if(targetGroup) {

          var allItems     = $(selectorFilterGroup),
              visibleItems = allItems.filter(function() {
                              var group = $(this).data('filterGroup');
                              return ('all' == targetGroup || group == targetGroup);
                            });

          allItems.parent() // select the col- element
                  .hide()   // Hide them
                  //.removeClass('elementHasBeenFiltered')
                  ;
          visibleItems.parent()   // select the col- element
                      .show()     // display and run the animation
                      .animo( { animation: itemAnimation, duration: 0.5} )
                      //.addClass('elementHasBeenFiltered')
                      ;

          // active by default de current trigger
          $this.addClass('active');
          // try to active the parent when in ul.nav element
          var res = $this.parents('ul').eq(0).children('li').removeClass('active');
          if(res.length) $this.parent().addClass('active');
        }
        
      });

  });

}(jQuery, window, document));
