function errorShow(error, element) {
  if ($(error).attr("errortext") == "|undefined") {
    return;
  }
  var errorText = $(error).html();
  if ($(element).attr("tip") != 1) {
    if (errorText == "") {
      return;
    }
    $(element).poshytip({
      className: 'tip-yellowsimple',
      alignTo: 'target',
      alignX: 'center',
      alignY: 'bottom',
      offsetX: 0,
      offsetY: 5,
      fade : false,
      slide : false,
      content: errorText
    });
    $(element).attr("tip", 1);
    $(element).poshytip('show');
  } else {
    $(element).poshytip("enable");
    $(element).poshytip('show');
    if (errorText == "") {
      return;
    }
    $(element).poshytip('update', errorText);
  }
  $(element).addClass("valid-error");
  $("label[target='" + $(element).attr("id") + "']").addClass("valid-error");
}

$.extend($.validator.defaults, {
  success: function(label){
    var $target = $("#" + $(label).attr("for"));
    $target.poshytip("disable");
    $target.removeClass("valid-error");
    $target.removeClass("error");
    $("label[target='" + $target.attr("id") + "']").removeClass("valid-error");
  },
  unhighlight: function(element) {
    $(element).poshytip("disable");
    $(element).removeClass("valid-error");
     $("label[target='" + $(element).attr("id") + "']").removeClass("valid-error");
  },
  unsuccess: function(error, element){
    errorShow(error, element);
  },
  errorPlacement: function(error, element){
    errorShow(error, element)
  }
});