/*!
 * @fileoverview table select plugins
 * @Version 1.0  (August 13, 2012)
 * @author FengWeiping 360buy.com
 * @email me@itfe.org
 * @example
 * $('#tableId').tableCheck(),
 */
/*global jQuery:true */
(function ($) {
  $.fn.extend({
    "tableCheck": function (options) {
      //default options
      options = $.extend({
        selected: "selected",
        checkControl: ".checkControl"
      }, options);

      //select all

      $("thead tr :checkbox,tr.thead :checkbox", this).click(function () {
        $(this).parents("table").find('tbody tr :checkbox').not('::disabled').not('tr.thead :checkbox')
            .attr("checked", this.checked).parents("tr")[this.checked ? "addClass" : "removeClass"](options.selected); //2011-5-31 fengweiping edited
      });
      //select one
      $('tbody tr :checkbox', this).not('tr.thead :checkbox').click(function () {
        var hasSelected = $(this).parents("tr").hasClass(options.selected);
        $(this).parents("tr")[hasSelected ? "removeClass" : "addClass"](options.selected);
        var $tmp = $(this).parents("table").find('tbody tr :checkbox').not('::disabled').not('tr.thead :checkbox');
        $(this).parents("table").find('thead tr :checkbox,tr.thead :checkbox')
            .attr('checked', $tmp.length == $tmp.filter(':checked').length);

      });

      //if one checkbox is checked ,then color this tr
      $('tbody>tr:has(:checkbox:checked)', this).addClass(options.selected);

    }
  });
})(jQuery);

//global event
$(function () {
  globalEvent.gridRows();
  globalEvent.menu();
});

var globalEvent = {
  /*color of grid's tr*/
  gridRows: function () {
    $('.grid tbody tr').bind('mouseover', function () {
      $(this).addClass('trHover').find('td:first').addClass('trHover').end().siblings().removeClass('trHover').find('td').removeClass('trHover');

    });
  },
  /*main menu*/
  menu: function () {
    $('#menu li a').bind('click', function () {
      if ($(this).next('ul').length) {
        $(this).toggleClass('closed').toggleClass('open').next('ul').slideToggle();
        $(this).parent().siblings().children('a').not('.noNext').addClass('closed').removeClass('open').next('ul').slideUp();
      } else {
        $(".selected").removeClass('selected');
        $(this).addClass('selected');
        if ($(this).hasClass('tit_lev1')) {
          $('.tit_lev1').not('.noNext').addClass('closed').removeClass('open').next('ul').slideUp();
        }
        if ($(this).hasClass('tit_lev2')) {
          $('.tit_lev2').not('.noNext').addClass('closed').removeClass('open').next('ul').slideUp();
        }
        if ($(this).hasClass('tit_lev3')) {

        }
      }
    });

    $('#menu li a').each(function () {
      if ($(this).next('ul:visible').length) {
        $(this).addClass('open');
      } else if ($(this).next('ul:hidden').length) {
        $(this).addClass('closed');
      } else {
        $(this).addClass('noNext');
      }
    });
    $('#menu a').bind('focus', function () {
      if (this.blur) {
        this.blur();
      }
    });

    $(window).load(function () {
      var scrollOffsetH = $(".totalScrollOffset").height();
      try {
        $(".menu-container").mCustomScrollbar({
          scrollButtons: {
            enable: true
          },
          advanced:{
            updateOnContentResize: true,
            updateOnBrowserResize: true
          },
          callbacks: {
            onTotalScroll: function () {  //滚动滚动条时动态添加内容
              //appendTextOnTotalScroll();
            },
            onTotalScrollOffset:  scrollOffsetH
          }
        });
      } catch (e) {}
    });
  }
}