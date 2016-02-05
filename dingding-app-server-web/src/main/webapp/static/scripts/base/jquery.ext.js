//表单序列化函数
$.fn.serializeObject = function() {
	var params = this.serializeArray();
    var paramObj = {};
	$.each(params, function(index, value) {
		if(!value || value.value == "" || value.value == null || value.value == 'undefined'){
			return;
		}
		if (!paramObj[value.name]) {
			paramObj[value.name] = [];
		}
		paramObj[value.name].push(value.value);
//        alert(value.name);
//        alert(value.value);
	});
	
	return paramObj;
};

//覆盖jquery val方法
(function(oldVal) {
  $.fn.val = function(value) {
    if ( arguments.length )
      return oldVal.call( this, value === undefined || value ===
          null ? "" : value );
    else
      return oldVal.call( this );
  };
})($.fn.val);

