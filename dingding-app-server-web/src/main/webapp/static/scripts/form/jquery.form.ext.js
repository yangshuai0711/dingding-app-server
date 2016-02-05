/**
 * 扩展jquery.form.js
 * 
 * @author Wuqingming
 */
(function($) {
	$.fn.serializeJSON = function() {
		var json = {};
		$.map($(this).serializeArray(), function(n, i) {
			var value = json[n['name']];
			if (value) {
				if ($.isArray(value)) {
					value.push(n['value']);
				} else {
					var array = new Array();
					array.push(value);
					array.push(n['value']);
					value = array;
				}
			} else {
				value = n['value'];
			}
			json[n['name']] = value;
		});
		return json;
	};

	$.fn.bindData = function(opts) {
		var defaults = {
			prefix : "",
			data : {}
		};
		var options = $.extend(defaults, opts);
		var bindObj = $(this);
		$.each(options.data, function(key, value) {
			if (options.prefix != "") {
				key = options.prefix + "." + key;
			}
			//key = key.toLowerCase();
			var elements = $(bindObj).find("[name='" + key + "']");
			if ($(elements).length == 0) {
				elements = $(bindObj).find("[id='" + key + "']");
			}
			if ($(elements).length == 0) {
				return true;
			}
			var tagName = elements.attr('tagName').toLowerCase();
			var type = elements.attr("type");
			if (type == undefined) {
				type = "";
			} else {
				type = type.toLowerCase();
			}
			if (tagName == "input" && (type == "radio" || type == "checkbox")) {
				var valueArray = $.isArray(value) ? value : [value];
				$(elements).each(function() {
					for (var i in valueArray) {
						if (this.value == valueArray[i]) {
							this.checked = true;
						}
					}
				});
			} else if (tagName == "input" || tagName == "textarea"
					|| tagName == "select") {
				$(elements).val(value);
			} else {
				$(elements).html(value);
			}
			return true;
		});
	};
})(jQuery);
