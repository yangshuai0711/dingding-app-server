/**
 * Panel组件，内含Tab控件
 * @author wuqingming
 */
!function($) {
  $.fn.mpanel = function(options) {
    var newoptions = {};
    newoptions = $.extend(newoptions, options);
    var $this = $(this);
    function initBasic() { //初始化基础信息
      $this.addClass( "ui-mpanel" );
      var titleObj = $this.find("div.mpanel-header .mpanel-header-title");
      var bodyObj = $this.find("div.mpanel-body");
     /* if (titleObj.length > 0 && bodyObj.length > 0) {
        titleObj.die().live('click', function (e) {
          if (!newoptions["collapsed"]) {
            bodyObj.hide(200);
            newoptions["collapsed"] = true;
          } else {
            bodyObj.show(200);
            newoptions["collapsed"] = false;
          }
        });
      }*/
      if (titleObj.length > 0 && newoptions["title"]) {
        titleObj.html(newoptions["title"]);
      }
    }
    function initTabs() {   //初始化Tab
      var menuItems = $this.find("div.mpanel-header ul.tab-menu li");
      if (menuItems.length == 0) {
        return;
      }/*
      if ( $.browser.msie &&  $.browser.version == "8.0" ) {
        menuItems.css("margin-top", "1px");
      } else if ( $.browser.safari ) {
        menuItems.css("margin-top", "1px");
      }*/
      $this.find("div.mpanel-header ul.tab-menu li a").die().live('click', function (e) {
        e.preventDefault();
      });
      $(menuItems).die().live('click', function (e) {
        var tabSelector = $(this).find("a").attr("href");
        setActiveTab(tabSelector);
        if (newoptions.tabClickHandler) {
          newoptions.tabClickHandler(e, tabSelector.substring(1));
        }
      });
      $.each( menuItems, function(i, menuItem){
        var tabSelector = $(menuItem).find("a").attr("href");
        $this.find(tabSelector).hide();
        if (!newoptions.activeTab && $(menuItem).hasClass("active")) {
          newoptions.activeTab = tabSelector.substring(1);
        }
      });
      if (newoptions.activeTab) {
        setActiveTab("#" + newoptions.activeTab);
      }
    }
    function setActiveTab(tabSelector) {       //设置活动tab
      var currentActiveTab = $this.find(tabSelector);
      var previosActiveMenuItem = $this.find("div.mpanel-header ul.tab-menu li.active");
      $this.find("div.mpanel-header ul.tab-menu li").removeClass("active");
      $this.find('div.mpanel-header ul.tab-menu li a[href="'+ tabSelector + '"]').parent().addClass("active");
      if (currentActiveTab.length == 0) {
        return;
      }
      if (previosActiveMenuItem.length > 0 && previosActiveMenuItem.find("a").attr("href").length > 0) {
        previosActiveMenuItem = $(previosActiveMenuItem.find("a").attr("href"));
        previosActiveMenuItem.hide();
      }
      currentActiveTab.show();
    }
    function initToolbar() {
      var toolbar = $this.children("div.mpanel-header").find("ul.mpanel-toolbar");
      if (toolbar.length == 0) {
        return;
      }
      var refresh = toolbar.find("li a[action='refresh']");
      var collapse = toolbar.find("li a[action='collapse']");
      var bodyObj = $this.children("div.mpanel-body");

      if (collapse.length > 0 && bodyObj.length > 0) {
        collapse.attr("title", "隐藏");
        collapse.unbind('click');
        collapse.bind('click', function (e) {
          var collapsedYClass = "icon-chevron-down";
          var collapsedNClass = "icon-chevron-up";
          if (!newoptions["collapsed"]) {
            bodyObj.hide(200);
            newoptions["collapsed"] = true;
            collapse.find("i").removeClass(collapsedNClass);
            collapse.find("i").addClass(collapsedYClass);
            collapse.attr("title", "展开");
          } else {
            bodyObj.show(200);
            newoptions["collapsed"] = false;
            collapse.find("i").removeClass(collapsedYClass);
            collapse.find("i").addClass(collapsedNClass);
            collapse.attr("title", "隐藏");
          }
        });
      }
      if (refresh.length > 0) {
        refresh.attr("title", "刷新");
        refresh.unbind('click');
        refresh.bind('click', function (e) {
          if (newoptions.refreshCallback && $.isFunction(newoptions.refreshCallback)) {
            newoptions.refreshCallback();
          }
        });
      }
    }
    function init() {
      initBasic();
      initTabs();
      initToolbar();
    }
    init();
    return {
      setOption: function(args) {
        newoptions = $.extend(newoptions, args);
        init();
      },
      getOption: function() {
        return newoptions;
      },
      setActiveTab: function(tabID) {
        newoptions.activeTab = tabID;
        setActiveTab("#" + tabID);
      }
    };
  };
}(window.jQuery);