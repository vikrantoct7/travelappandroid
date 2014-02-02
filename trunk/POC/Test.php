<html>
<head>
<script src="jquery-1.5.2.min.js" ></script>
<script language='javascript'>
/*$( document ).ready(function() {
$.ajax({ //Process the form using $.ajax()
			 url         : 'hello1.php', //Your form processing file url
            type        : 'GET', //Method type
			contentType : "application/json; charset=utf-8",
            //data        : {id, 45}, //Forms name
            dataType    : 'json',
            success     : function(data) {
            alert(data.e);
            }
        });
});*/
// This just displays the first parameter passed to it
// in an alert.
function show(json) 
{
alert('hhhh11');	
alert(json);
}
function run() 
{	
var data = {name:"Jack", age:12, place:"CA", marks:70};

$.ajax({ //Process the form using $.ajax()
			url         : 'hello.php', //Your form processing file url
            type        : 'POST', //Method type
			//contentType : "application/json; charset=utf-8",
            data        : data, //Forms name
            dataType    : 'json',
            success     : function(data) {
            alert('Name:'+data.name + ' ,age:'+data.age+ ' ,place:'+data.place+ ' ,marks:'+data.marks);
            }
        });

}

// We'll run the AJAX query when the page loads.
window.onload=run;
</script>
</head><body>JSON Test Page.
</body>
</html>