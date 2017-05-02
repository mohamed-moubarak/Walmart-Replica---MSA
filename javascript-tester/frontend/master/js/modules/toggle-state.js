/**=========================================================
 * Module: toggle-state.js
 * Toggle a classname from the BODY. Useful to change a state that 
 * affects globally the entire layout or more than one item
 * Targeted elements must have [data-toggle="CLASS-NAME-TO-TOGGLE"]
 * Optionally save and restore state [data-persists="true"]
 =========================================================*/

(function($, window, document){
  'use strict';

  var SelectorToggle  = '[data-toggle-state]',
      $body           = $('body'),
      storageKeyName  = 'toggleState';


  $(document)
    .ready(function() {
      restoreState($body);
    })
    .on('click', SelectorToggle, function (e) {
      e.preventDefault();
      var classname = $(this).data('toggleState'),
          persists  = $(this).data('persists');
      
      if(classname) {
        if( $body.hasClass(classname) ) {
          $body.removeClass(classname);
          if(persists) removeState(classname);
        }
        else {
          $body.addClass(classname);
          if(persists) addState(classname);
        }
        
      }

  });

  // Add a state to the browser storage to be restored later
  function addState(classname){
    var data = store.get(storageKeyName);
    
    if(!data)  {
      data = classname;
    }
    else {
      data = WordChecker.addWord(data, classname);
    }

    store.set(storageKeyName, data);
  }

  // Remove a state from the browser storage
  function removeState(classname){
    var data = store.get(storageKeyName);
    // nothing to remove
    if(!data) return;

    data = WordChecker.removeWord(data, classname);

    store.set(storageKeyName, data);
  }
  
  // Load the state string and restore the classlist
  function restoreState($elem) {
    var data = store.get(storageKeyName);
    
    // nothing to restore
    if(!data) return;
    $elem.addClass(data);
  }


  //////////////////////////////////////////////////
  // Helper object to check for words in a phrase //
  //////////////////////////////////////////////////
  var WordChecker = {
    hasWord: function (phrase, word) {
      return new RegExp('(^|\\s)' + word + '(\\s|$)').test(phrase);
    },
    addWord: function (phrase, word) {
      if (!this.hasWord(phrase, word)) {
        return (phrase + (phrase ? ' ' : '') + word);
      }
    },
    removeWord: function (phrase, word) {
      if (this.hasWord(phrase, word)) {
        return phrase.replace(new RegExp('(^|\\s)*' + word + '(\\s|$)*', 'g'), '');
      }
    }
  };


}(jQuery, window, document));
