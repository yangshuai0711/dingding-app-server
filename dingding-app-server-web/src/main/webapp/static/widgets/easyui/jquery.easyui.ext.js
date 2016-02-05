/**
 * Easyui扩展
 */
var DataGridExt = {
  calDatagridAllColumnWidth: function (gridSelector) { //计算datagrid所有列宽
    var grid = $(gridSelector).parent().parent();
    var columnFields = $(gridSelector).datagrid('getColumnFields');
    var allColumnWidth = 0;
    var columnCount = 0;
    var checkboxCount = 0;
    $.each(columnFields, function (i, columnField) {
      var columnFieldOption = $(gridSelector).datagrid('getColumnOption', columnField);
      if (columnFieldOption.hidden == "true" || columnFieldOption.hidden == true) {
      } else if (columnFieldOption.checkbox != true) {
        allColumnWidth += columnFieldOption.width - 8;   //每个column宽都会减8
        columnCount++;
      } else if (columnFieldOption.checkbox == true) {
        checkboxCount++;
      }
    });
    return {allColumnWidth: allColumnWidth, columnCount: columnCount, checkboxCount: checkboxCount};
  },
  setupDatagridColumnWidth: function (a, _gridSelector_) {  //设置datagrid列宽
    var gridSelector = _gridSelector_ || this;
    var options = $(gridSelector).datagrid('options');
    var grid = $(gridSelector).parent().parent();
    var view2 = $(grid).find(".datagrid-view").find(".datagrid-view2");
    var viewContentWidth = $(view2).width();
    var allColumnWidthJson = DataGridExt.calDatagridAllColumnWidth(gridSelector);
    var allColumnWidth = allColumnWidthJson.allColumnWidth;
    var columnCount = allColumnWidthJson.columnCount;
    var checkboxCount = allColumnWidthJson.checkboxCount;
    var baseDistanceWidth = viewContentWidth - allColumnWidth - (columnCount + checkboxCount) * 9;
    var distanceWidth = checkboxCount > 0 ? baseDistanceWidth - (checkboxCount * (27 - 8)) : baseDistanceWidth;
    var addWidthPercent = distanceWidth / allColumnWidth;
    var columnFields = $(gridSelector).datagrid('getColumnFields');
    var totalDistanceWidth = 0;
    var lastColumnField = null;
    $.each(columnFields, function (i, columnField) {
      var columnFieldOption = $(gridSelector).datagrid('getColumnOption', columnField);
      if (columnFieldOption.checkbox != true && columnFieldOption.hidden != "true") {
        var columnOrginWidth = columnFieldOption.width - 8;
        var distanceWidth = Math.floor(columnOrginWidth * addWidthPercent);
        totalDistanceWidth += distanceWidth;
        var targetWidth = columnOrginWidth + distanceWidth;
        if (targetWidth <= columnOrginWidth) {
          targetWidth = columnOrginWidth;
        }
        lastColumnField = columnField;
        view2.find(".datagrid-header").find("td[field=\"" + columnField + "\"] div.datagrid-cell").width(targetWidth);
        view2.find(".datagrid-body").find("td[field=\"" + columnField + "\"] div.datagrid-cell").width(targetWidth);
      }
    });
    if (Math.abs(distanceWidth - totalDistanceWidth) >= 1 && lastColumnField) {  //将算的差值放入最后一列
      var columnFieldOption = $(gridSelector).datagrid('getColumnOption', lastColumnField);
      var columnOrginWidth = columnFieldOption.width - 8;
      var distanceWidth = parseInt(columnOrginWidth * addWidthPercent) + (distanceWidth - totalDistanceWidth);
      var targetWidth = columnOrginWidth + distanceWidth;
      if (targetWidth <= columnOrginWidth) {
        targetWidth = columnOrginWidth;
      }
      view2.find(".datagrid-header").find("td[field=\"" + lastColumnField + "\"] div.datagrid-cell").width(targetWidth);
      view2.find(".datagrid-body").find("td[field=\"" + lastColumnField + "\"] div.datagrid-cell").width(targetWidth);
    }
    // alert("viewContentWidth = " + viewContentWidth + ", allColumnWidth = " + allColumnWidth + ", distanceWidth = " + (viewContentWidth - allColumnWidth));
  },
  escapeFormatter: function(value, rowData, rowIndex) {  //HTML转义、防止xss攻击
    if (value == null || value == "" || value == undefined) {
      return "";
    }
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
  },
  setCheckAllNotCheck : function(gridSelector) {
    var grid = $(gridSelector).parent().parent();
    var gridHeader = $(grid).find(".datagrid-view").find(".datagrid-view2").find(".datagrid-header");
    var checkAll = gridHeader.find(".datagrid-header-check").find("input[type='checkbox']");
    if (checkAll.length > 0) {
      checkAll.attr("checked", false);
    }
  }
}/*

 $.extend($.fn.datagrid.defaults.view, {
 onBeforeRender: function (target, rows) {
 if (rows == null || rows.length == 0) {
 return;
 }
 var opts = $(target).datagrid("getColumnOption");
 var a = 1;
 var b = 1;
 }
 });*/

$.extend($.fn.datagrid.defaults, {
  onOpen: function () {
    var targetDiv = $(this).find(".datagrid-view").find("div");
    var gridSelector = "#" + $(targetDiv[targetDiv.length - 1]).attr("id");

    $(gridSelector).datagrid('resize');
    var options = $(gridSelector).datagrid('options');
    if (options.onLoadSuccess && options.onLoadSuccess.toString().indexOf("setupDatagridColumnWidth") < 0) {
      options.addLoadSuccessListener = true;
      var oldOnLoadSuccess = options.onLoadSuccess;
      options.onLoadSuccess = function (a) {
        oldOnLoadSuccess(a);
        DataGridExt.setupDatagridColumnWidth(a, gridSelector);
      };
    }
    var columnOptions = options.columns[0];
    for (var i in columnOptions) {
      var columnOption = columnOptions[i];
      if (columnOption.checkbox == true || columnOption.escape == false || columnOption.formatter) {
        continue;
      }
      columnOption.formatter = DataGridExt.escapeFormatter;
    }
    var isDialog = false;
    var $parent = $(gridSelector).parent();
    for (var i = 0; i < 10; i++) {
      if ($parent.length == 0) {
        break;
      }
      if ($parent.hasClass("aui_content")) {
        isDialog = true;
        break;
      }
      $parent = $parent.parent();
    }
    if (!isDialog) {
      $(window).resize(function () {
        $(gridSelector).datagrid('resize');
      });
    }
  },
  onResize: function (a, b) {
    var grid = this;
    var gridWidth = $(grid).width();
    var gridWidthAttr = $(grid).attr("width");
    $(grid).attr("width", gridWidth);
    if (gridWidthAttr == gridWidth) {
      return;
    }
    var targetDiv = $(grid).find(".datagrid-view").find("div");
    var gridSelector = "#" + $(targetDiv[targetDiv.length - 1]).attr("id");
    DataGridExt.setupDatagridColumnWidth(null, gridSelector);
  },
  onLoadSuccess: function (data) {
    DataGridExt.setupDatagridColumnWidth(null, this);
    /*if (a && a.status && a.status.result == "success") {
     a.rows = a.result;
     a.result = null;
     a.total = a.totalCount;
     } else */
    if (data && data.status && data.status.result == "failure") {
      alert("数据查询失败");
      return;
    }
    DataGridExt.setCheckAllNotCheck(this);
  },
  onLoadError: function () {
   // alert("error");
  },
  pageSize: 20,
  rownumbers: false,
  loadMsg: '数据加载中，请稍后...',
  loadingMessage: '数据加载中，请稍后...'
});
$.extend($.fn.datagrid.methods, {
  addToolbarItem: function (jq, items) {
    return jq.each(function () {
      var dpanel = $(this).datagrid('getPanel');
      var toolbar = dpanel.children("div.datagrid-toolbar");
      if (!toolbar.length) {
        toolbar = $("<div class=\"datagrid-toolbar\"><table cellspacing=\"0\" cellpadding=\"0\"><tr></tr></table></div>").prependTo(dpanel);
        $(this).datagrid('resize');
      }
      var $parentContainer = toolbar;
      for (var i = 0; i < items.length; i++) {
        var item = items[i];
        if (item == "-") {
          $("<div class=\"datagrid-btn-separator\"></div>").appendTo($parentContainer);
        } else if (item == '->') {
          $("<div class=\"datagrid-toolbar-container\" style='float: right;'></div>").appendTo($parentContainer);
          var $parentContainer = toolbar.children("div.datagrid-toolbar-container");
        } else if (item.html) {
          $("<div style='float: left;'>" + item.html + "</div>").appendTo($parentContainer);
        } else {
          var b = $("<a href=\"javascript:void(0)\"></a>").appendTo($parentContainer);
          b[0].onclick = eval(item.handler || function () {
          });
          b.linkbutton($.extend({}, item, {
            plain: true
          }));
        }
      }
    });
  },
  resizeColumnWidth: function (gridSelector) {
    DataGridExt.setupDatagridColumnWidth(null, gridSelector);
  }
});