!function($) {
	var MsgBox = function (options) {
		this.id = options.id || Math.floor(Math.random() * 10 + 1);
		this.selector = options.parentid || options.selector;
		this.type = options.type;
		this.title = options.title;
		this.content = options.content || '';
		this.closeable = (options.closeable == false) ? false : true;
		this.closeCallback = options.closeCallback;
		this.global = options.global || 'n';
		this.build();
	};
	MsgBox.prototype = {
		constructor : MsgBox,
		build	: function () {
			var noTitle = this.title ? false : true;
			var html = '<div id="' + this.id + '" class="msgbox ' + this.type;
      html += this.global == 'y' ? ' global">' : '">';
      html += this.closeable ? ' <a class="close" title="关闭" href="#"><i class="icon-close"></i></a>' : '';
			html += noTitle ? '' : '<div class="title">' + this.title + '</div>' ;
			html +='<div class="content">' + this.content + '</div>';
			html += '</div>';
			if (this.global == 'y') {
				$(document).find(".msgbox.global").remove();
				$(document.body).append(html);
				//$(document).find(".global-msg").slideToggle(5000);
			} else {
				$(this.selector).html(html);
			}
      if (this.closeable) $(this.selector).find(".close").bind("click", this.close);
		},
		close : function(e) {
			if (this.global == 'y') {
        $(document).find(".msgbox.global").remove();
				if (this.closeCallback) {
					this.closeCallback();
				}
			} else {
				$(this).addClass("hidden");
				if (this.closeCallback) {
					this.closeCallback();
				}
			}
		}
	};
	$.fn.showmsg = function(options) {
    options = options || {};
    options.selector = this.selector;
		return new MsgBox(options);
	};
	$.fn.showGlobalMsg = function(options) {
    options.global = 'y';
		return new MsgBox(options);
	};
}(window.jQuery);