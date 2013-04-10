
$.validator.addMethod("newName", function (value) {
  var r;
  r = jsRoutes.controllers.Feeds.validName("michele");
    
  var ret = true;
  $.ajax({
    url: r.url,
    type: r.type,
    async: false,
    dataType: 'json',
    data: {
      name: value
    },
    success: function(data) {
      ret = true;
    },
    error: function(err) {
      ret = false;
    }
  }).responseText;

  return ret
}, "Please select a new name")


$.validator.addMethod("regularName", function (value) {
  return /^[a-z0-9]+$/i.test(value)
}, "Name must be alphanumeric")

$().ready(function() {
	// validate the comment form when it is submitted
	$("#newFeedForm").validate({
    rules: {
      name: {
        required: true,
        minlength: 3,
        maxlength: 25,
        newName: "newName",
        regularName: "regularName"
      }, 
      messages: {
        name: {
          required: "Please enter a name",
          minlength: "Name must be at least 3 characters",
          maxlength: "Name must be less then 25 characters",
          taken: "Name already taken"
        }
      },
      highlight: function(element) {
        $(element).closest('.control-group').removeClass('success').addClass('error');
      },
      success: function(element) {
        element
        .text('OK!').addClass('valid')
        .closest('.control-group').removeClass('error').addClass('success');
      }
    }
  });

  // validate signup form on keyup and submit
  $("#signupForm").validate({
    rules: {
      name : {
        required: true,
        minlength: 2,
        maxlength: 25,
        regularName: "regularName"
      }, 
      email: true,
			passw1: {
				required: true,
        maxlength: 25,
				minlength: 5
			},
			passw2: {
				required: true,
				minlength: 5,
				equalTo: "#passw1"
			},
    }, messages: {
      name: {
        required: "Please enter a name",
        minlength: "Name must be at least 3 characters",
        maxlength: "Name must be less then 25 characters",
      }, 
      email: "Please enter a valid email address",
      passw1: {
				required: "Please provide a password",
				minlength: "Your password must be at least 5 characters long",
        maxlength: "Password must be less then 25 characters",
			},
			passw2: {
				required: "Please provide a password",
				minlength: "Your password must be at least 5 characters long",
				equalTo: "Please enter the same password as above"
			},
    },
    highlight: function(element) {
      $(element).closest('.control-group').removeClass('success').addClass('error');
    },
    success: function(element) {
      element
      .text('OK!').addClass('valid')
      .closest('.control-group').removeClass('error').addClass('success');
    }
  });

});

