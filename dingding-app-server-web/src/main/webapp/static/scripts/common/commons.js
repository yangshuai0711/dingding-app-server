/**
 * 公共js封装组件
 *
 * @author NIXIANG
 * @author Wuqingming
 *
 * {@link Commons.parent()} 功能页中获取父级页面, 直接用<b>parent</b>会获取到最外层的frameset
 * {@link Commons.random()} 获取随机数
 * {@link Commons.replace()} 跳转页面
 *
 * @type {*}
 */

/**
 * 计算datagrid高度
 */
function calDatagridBaseHeight() {
  var baseHeight = $(window).height() - 38 - (30 + 20 + 46 + 5);
  var screenWidth = screen.width;
  var screenHeight = screen.height;
  if (screenWidth <= 1024) {
    baseHeight -= 16;
  }
  return baseHeight;
}
Commons = (function () {
  return {
    config: {
      datagrid_base_height1: calDatagridBaseHeight(),
      datagrid_base_height2: calDatagridBaseHeight() - 34,
      datagrid_base_height3: calDatagridBaseHeight() - (34 + 34),
      modal_dialog_btnbar_height1: 70,
      default_process_msg: "请求处理中，请稍后……",
      emptyDictArray: [{code: '', name: '请选择'}],
      responseStatus : {
        SUCCESS: "success",      //成功
        FAILURE: "failure",      //失败
        WARNING: "warning"       //警告
      },
      optype: {  //操作类型
        ADD : 1,
        UPDATE: 2
      }
    },
    renderer : {
      dictRender : function (dictArray, dictCode) {		//字典转化
        if (dictArray == null || dictArray.length == 0) {
          return "";
        }
        if (Commons.isEmpty(dictCode)) {
          return "";
        }
        for(var i = 0;i < dictArray.length;i++){
          var dictItem = dictArray[i];
          if(dictItem.code == dictCode) {
            return dictItem.name;
          }
        }
        return dictCode;
      },
      dateRender : function (value) {						//日期转化
        if (Commons.isEmpty(value)) {
          return "";
        }
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        return value.match(reDate);
      },
      timeRender : function (value) {						//时间转化
        if (Commons.isEmpty(value)) {
          return "";
        }
        var reDate = /\d{4}\-\d{2}\-\d{2}/gi;
        var reTime = /\d{2}:\d{2}:\d{2}/gi;
        return value.match(reDate) + " " + value.match(reTime);
      }
    },
    nullToString : function (obj) {
      if(obj == null || obj == "null" || obj == "undefined"){
        return "";
      }
      return obj;
    },
    isEmpty : function (obj) {
      return (obj == null || obj == "null" || obj == "undefined" || obj.length == 0);
    },
    trim : function (str) {
      return str.replace(/(^s*)|(s*$)/g, "");
    },
    createHTMLElsFromDict : function (dictArray, pElId, config) {
      if (dictArray == null || dictArray.length == 0) {
        return;
      }
      if (!config || !config.type) {
        alert("调用Common.createHTMLElsFromDict时参数config为空或config.type为空.");
        return;
      }
      var html = '';
      for (var i = 0; i < dictArray.length; i++) {
        if (config.type == "radio" || config.type == "checkbox") {
          html += '&nbsp;&nbsp;<input type="' + config.type + '" value="' + dictArray[i].code + '"';
          if (config.elId) {
            html += ' id="' + config.elId + '"';
          }
          if (config.elName) {
            html += ' name="' + config.elName + '"';
          }
          if ((config && config.defaultValue == dictArray[i].code) || ((!config || !config.defaultValue) && i == 0)) {
            html += ' checked="checked"';
          }
          if (config.elClass) {
            html += ' class="' + config.elClass + '"';
          }
          html += '/>&nbsp;' + dictArray[i].value + '&nbsp;&nbsp;';
        } else if (config.type == "option") {
          html += '<option value="' + dictArray[i].code + '"';
          if (config.elId) {
            html += ' id="' + config.elId + '"';
          }
          if (config.elName) {
            html += ' name="' + config.elName + '"';
          }
          if ((config && config.defaultValue == dictArray[i].code) || ((!config || !config.defaultValue) && i == 0)) {
            html += ' selected="selected"';
          }
          html += '>' + dictArray[i].name + '</option>';
        }
      }
      $("#" + pElId).html(html);
    },

    /**
     * 功能页中获取父级页面, 直接用<b>parent</b>会获取到最外层的frameset
     *
     * @return {*}
     */
    parent: function () {
      return parent.frames["main"];
    },

    /**
     * 获取绝对路径URL
     * @param url
     */
    absoluteUrl: function(url) {
      return "/privilege-console" + url;
    },

    /**
     * 获取随机数
     *
     * @return {Number}
     */
    random: function () {
      return parseInt(10000 * Math.random());
    },

    /**
     * 跳转页面
     *
     * @param _url_ {@link window.location} href
     * @param _address_ 地址
     */
    replace: function (_url_, _address_) {
      _url_.replace(_address_);
    },

    /**
     * 弹出窗口
     *
     * @param _context_ 内容
     * @param _title_ 标题
     * @param _width_ 长度
     * @param _type_ 类型
     * @param _call_back_ 回调函数
     */
    alert: function (_context_, _title_, _width_, _type_, _call_back_) {
      /* if (undefined == _width_) {
       _width_ = 160;
       }*/
      var icon = "warning";
      if (_type_ == "error") {
        icon = "error";
      } else if (_type_ == "alert") {
        icon = "warning";
      } else if (_type_ == "attention") {
        icon = "attention";
      } else if (_type_ == "success") {
        icon = "succeed";
      }
      var dialog = $.dialog({
        title: _title_,
        content: _context_,
        // width: _width_,
        lock: true,
        fixed: true,
        icon: icon,
        close: function () {
          if (_call_back_) {
            setTimeout(function () {
              _call_back_();
            }, 100);
          }
          return true;
        },
        ok: true
      });
      return dialog;
    },

    /**
     * 错误提示
     *
     * @param _context_ 内容
     * @param _title_ 标题
     * @param _width_ 长度
     * @param _call_back_ 回调函数
     */
    showError: function (_context_, _title_, _width_, _call_back_) {
      if (undefined == _title_ || "" == _title_) {
        _title_ = "错误提示";
      } else {
        if ($.isFunction(_title_)) {
          _call_back_ = _title_;
          _title_ = "错误提示";
        } else if (typeof(_title_) == "number") {
          if (undefined != _width_) {
            if ($.isFunction(_width_)) {
              _call_back_ = _width_;
            }
          }
          _width_ = _title_;
          _title_ = "错误提示";
        }
      }
      if (undefined != _width_) {
        if ($.isFunction(_width_)) {
          _call_back_ = _width_;
          _width_ = undefined;
        }
      }
      return Commons.alert(_context_, _title_, _width_, "error", _call_back_);
    },

    /**
     * 警告提示
     *
     * @param _context_ 内容
     * @param _title_ 标题
     * @param _width_ 长度
     * @param _call_back_ 回调函数
     */
    showWarn: function (_context_, _title_, _width_, _call_back_) {
      if (undefined == _title_ || "" == _title_) {
        _title_ = "警告提示";
      } else {
        if ($.isFunction(_title_)) {
          _call_back_ = _title_;
          _title_ = "警告提示";
        } else if (typeof(_title_) == "number") {
          if (undefined != _width_) {
            if ($.isFunction(_width_)) {
              _call_back_ = _width_;
            }
          }
          _width_ = _title_;
          _title_ = "警告提示";
        }
      }
      if (undefined != _width_) {
        if ($.isFunction(_width_)) {
          _call_back_ = _width_;
          _width_ = undefined;
        }
      }
      return Commons.alert(_context_, _title_, _width_, "alert", _call_back_);
    },

    /**
     * 普通提示
     *
     * @param _context_ 内容
     * @param _title_ 标题
     * @param _width_ 长度
     * @param _call_back_ 回调函数
     */
    showInfo: function (_context_, _title_, _width_, _call_back_) {
      if (undefined == _title_ || "" == _title_) {
        _title_ = "消息提示";
      } else {
        if ($.isFunction(_title_)) {
          _call_back_ = _title_;
          _title_ = "消息提示";
        } else if (typeof(_title_) == "number") {
          if (undefined != _width_) {
            if ($.isFunction(_width_)) {
              _call_back_ = _width_;
            }
          }
          _width_ = _title_;
          _title_ = "消息提示";
        }
      }
      if (undefined != _width_) {
        if ($.isFunction(_width_)) {
          _call_back_ = _width_;
          _width_ = undefined;
        }
      }
      return Commons.alert(_context_, _title_, _width_, "attention", _call_back_);
    },

    /**
     * 成功提示
     *
     * @param _context_ 内容
     * @param _title_ 标题
     * @param _width_ 长度
     * @param _call_back_ 回调函数
     */
    showSuccess: function (_context_, _title_, _width_, _call_back_) {
      if (undefined == _title_ || "" == _title_) {
        _title_ = "成功提示";
      } else {
        if ($.isFunction(_title_)) {
          _call_back_ = _title_;
          _title_ = "成功提示";
        } else if (typeof(_title_) == "number") {
          if (undefined != _width_) {
            if ($.isFunction(_width_)) {
              _call_back_ = _width_;
            }
          }
          _width_ = _title_;
          _title_ = "成功提示";
        }
      }
      if (undefined != _width_) {
        if ($.isFunction(_width_)) {
          _call_back_ = _width_;
          _width_ = undefined;
        }
      }
      var dialog = Commons.alert(_context_, _title_, _width_, "success", _call_back_);
      dialog.time(1);
      return dialog;
    },
    showModalDialog: function (_option) {
      var option = _option || {};
      $.dialog.open(option.url, {title: option.title, width: option.width, height: option.height, lock: true, padding: '6px', resize: false});
    },
    closeModalDialog: function (_option) {
      var option = _option || {};
      $.dialog.open.api.close();
      if (option && option.callback) {
        option.callback();
      }
    },
    getModalDialogParent: function () {
      return $.dialog.parent;
    },
    getModalDialogHeight: function () {
      return $(window).height() - 40;
    },
    getModalDialogWidth: function () {
      return $(window).width() - 20;
    },
    dialog: function (_option) {
      var option = _option || {};
      var defaults = {
        cancel: function () {
          this.hide();
          return false;
        },
        close: function () {
          this.hide();
          return false;
        }
      };
      option = $.extend(defaults, option);
      option.padding = "6px";
      option.lock = true;
      if (option.id) {
        var html = '<div id="' + option.id + '" ';
        var width = option.width;
        var height = option.height;
        if (option.width || option.height) {
          html += 'style="';
          if (!option.overflow && ((width && width != "auto") || (height && height != "auto"))) {
            html += 'overflow: auto;'
          } else if (option.overflow) {
            html += 'overflow: ' + option.overflow + ";"
          }
          if (width) {
            html += 'width: ' + (width - 6) + 'px;'
          }
          if (height) {
            html += 'height: ' + (height - 26) + 'px;'
          }
          html += '"'
        }
        html += '>' + option.content + '</div>';
        option.content = html;
      }
      $.dialog(option);
    },
    getDialogWidth: function () {
      return $(window).width() - 20;
    },
    getDialogHeight: function () {
      return $(window).height() - 96;
    },
    hideDialog: function (id) {
      $.dialog.list[id].hide();
    },
    showDialog: function (id) {
      $.dialog.list[id].show();
    },
    changeDialog: function (id, _option) {
      var option = _option || {};
      if (option.title) {
        $.dialog.list[id].title(option.title);
      }
      if (option.content) {
        $.dialog.list[id].content(option.content);
      }
    },
    confirm: function (_option) {
      var option = _option || {};
      $.dialog({
        width: option.width || 'auto',
        title: option.title || "确认提示",
        content: option.content,
        icon: 'question',
        lock:true,
        ok: function () {
          if (option.onConfirm) {
            option.onConfirm();
          }
          return true;
        },
        cancelVal: '关闭',
        cancel: true //为true等价于function(){}
      });
    },
    showProcessMsg: function (msg) {
      $.blockUI({ message: msg });
    },
    hideProcessMsg: function () {
      $.unblockUI();
    },
    addUrlParameter : function(url, param) {
      if (url.indexOf("?") < 0) {
        url += "?";
      }
      if (url.indexOf("&") > 0 || url.lastIndexOf("?") != (url.length - 1)) {
        url += "&";
      }
      return url + param;
    },
    selectOption : function(selector, value) {
      $(selector).find("option[value='" + value + "']").attr("selected", true);
    }
  }
})();

$(document).ready(function () {
  function adjustBasePageContentHeight() { //自适应高度
    if ($(".base-page-content").length > 0) {
      if ($.browser.msie) {
        $(".base-page-content").css("height", $(window).height() - 38);
      } else {
        $(".base-page-content").css("min-height", $(window).height() - 38);
      }
    }
  }

  function adjustSearchableWidth() {
    if ($.browser.msie && $.browser.version < 8.0) {
      return;
    }
    if ($(".base-page-content .search table").length > 0 && $(".search table tr").length > 1) {
      $(".base-page-content .search table").css("width", "98%");
      $(".base-page-content .search table tr td.form-text input[type='text']").css("width", "96%");
      $(".base-page-content .search table tr td.form-text select").css("width", "99.6%");/*
      $(".base-page-content .search table tr td.form-text *[required='true']").css("width", "80%");*/
    }
  }

  function initModalDialog() {
    if ($(".modal-dialog").length > 0) {
      $("body").addClass("modal-dialog-body");
    }
  }
  adjustBasePageContentHeight();
  adjustSearchableWidth();
  initModalDialog();
});

Date.prototype.format = function (format) {
  var o = {
    "M+": this.getMonth() + 1,
    "d+": this.getDate(),
    "h+": this.getHours(),
    "m+": this.getMinutes(),
    "s+": this.getSeconds(),
    "q+": Math.floor((this.getMonth() + 3) / 3),
    "S": this.getMilliseconds()
  }

  if (/(y+)/.test(format)) {
    format = format.replace(RegExp.$1, (this.getFullYear() + "")
      .substr(4 - RegExp.$1.length));
  }

  for (var k in o) {
    if (new RegExp("(" + k + ")").test(format)) {
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
        : ("00" + o[k]).substr(("" + o[k]).length));
    }
  }
  return format;
}
String.prototype.format = function(args) {
  var result = this;
  if (arguments.length > 0) {
    if (arguments.length == 1 && typeof (args) == "object") {
      for (var key in args) {
        if(args[key]!=undefined){
          var reg = new RegExp("({" + key + "})", "g");
          result = result.replace(reg, args[key]);
        }
      }
    } else {
      for (var i = 0; i < arguments.length; i++) {
        if (arguments[i] != undefined) {
          var reg= new RegExp("({)" + i + "(})", "g");
          result = result.replace(reg, arguments[i]);
        }
      }
    }
  }
  return result;
}

var sessionExpired = false;	//session已经失效
(function() {
  var oldAjaxFuc = jQuery.ajax;
  jQuery.extend({
    ajax: function( options ) {
      if (options.cache == null) {
        options.cache = false;
      }
      if (options.traditional == null) {
        options.traditional = true;
      }
      var oldSuccessFunc = options.success;
      var oldErrorFunc = options.error;
      var resolveHttpStatus = function(httpStatus, msg, code) {
        if(httpStatus == 401) {
          if (!sessionExpired) {
            sessionExpired = true;
            var msg = Commons.isEmpty(msg) ? "未认证或会话超时！" : decodeURI(msg);
            var dialog = Commons.showWarn(msg, function() {
              location.reload(true);
            });
            setTimeout(function() {
              dialog.close();
            }, 2000);
          }
          return true;
        } else if (httpStatus == 403) {
          Commons.showWarn("无访问权限！");
          return true;
        } else if (httpStatus == 500) {
          Commons.showError("系统内部错误！");
          return true;
        } else if (httpStatus == 200) {
          var msg = Commons.isEmpty(msg) ? "请求失败！" : decodeURI(msg);
          Commons.showError(msg);
          return true;
        }
        return false;
      }
      options.success = function(data, ajaxStatus, request) {
        var httpStatus = request.getResponseHeader("httpStatus");
        var errorFlag = false;
        if (!Commons.isEmpty(httpStatus)) {
          errorFlag = resolveHttpStatus(parseInt(httpStatus), request.getResponseHeader("msg"), request.getResponseHeader("code"));
        }
        if (oldSuccessFunc && !errorFlag) {
          oldSuccessFunc(data, ajaxStatus, request);
        }
      }
      options.error = function(ret) {
        var resolveResult = resolveHttpStatus(ret.status);
        if (oldErrorFunc && !resolveResult) {
          oldErrorFunc.apply(this, ret);
        }
      };
      oldAjaxFuc(options);
    }
  });
})();