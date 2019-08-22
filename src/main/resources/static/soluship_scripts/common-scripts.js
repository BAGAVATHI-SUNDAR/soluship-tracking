$(document).ready(function(){

 
/*  if($("p").hasClass( "has-tooltip" )){
    $(".charge-block").each(function() {    
     console.log($(this).find('small').html());
      var data=$(this).find('small').html();
      $(this).prev().attr("data-toggle","tooltip");
     $(this).prev().attr("data-trigger","click");
      $(this).prev().attr("data-placement","left");
      $(this).prev().attr("data-html","true");
      $(this).prev().attr("data-original-title",data);
     $(this).prev().tooltip("show");
     $(this).prev().css( "background-color", "red" );
    });

    $.each($('charge-block'), function (index, item) {
     console.log(item.innerHTML);
    });
  } */

  var show_btn=$('.show-modal');
  //var show_btn=$('.show-modal');
  //$("#testmodal").modal('show');
  
  show_btn.click(function(){
    $("#inv_search_filter").modal('show');
  })

  show_btn.click(function(){
    $("#edi_search_filter").modal('show');
  })

  $('.form_date').datetimepicker({
    //language:  'fr',
    weekStart: 1,
    todayBtn:  1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    forceParse: 0,
    format :'yyyy-mm-dd'
  });

});

 
  


 

function alertData(data){
  $("#alertDataDiv").html(data);
  $('#alertData').modal('show');
}


function fileinprogress() {
  alertData("File upload Still in process!");
}

function fileinpending() {
  alertData("File Sync in Pending. Please reupload again");
}





$('.modal').on('shown.bs.modal', function() {
  
});