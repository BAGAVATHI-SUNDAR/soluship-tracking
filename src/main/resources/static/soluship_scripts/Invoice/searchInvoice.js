$(document).ready( function () {
	// $('#ediFilesTable').DataTable();
	/*$('#ediFilesTable').dataTable( {
	  		"bJQueryUI": true,
            'iDisplayLength': 5,
            'bLengthChange': false
	});*/

	$('#ediFilesTable').DataTable({
		// "sAjaxSource": "/invoice-list",
		"sAjaxDataProp": "",
		'columnDefs': [
         {
            'targets': 0,
            'checkboxes': {
               'selectRow': true
            }
         }
      ],
      'select': {
         'style': 'multi'
      },
	});

	var table = $('#ediFilesTable12').DataTable({
		"sAjaxSource": "/invoice-list",
		"sAjaxDataProp": "",
		'columnDefs': [
         {
            'targets': 0,
            'checkboxes': {
               'selectRow': true
            }
         }
      ],
      'select': {
         'style': 'multi'
      },
		"order": [[ 0, "asc" ]],
			"aoColumns": [
			{
                data:   "invoiceId",
                render: function ( data, type, row ) {
                    if ( type === 'display' ) {
                        return '<input type="checkbox" class="editor-active" value="'+data+'">';
                    }
                    return data;
                },
                className: "dt-body-center"
            },
			{ "mData": "invoiceNum" },
			{ "mData": "customerName" },
			{ "mData": "invoiceDate" },
			{ "mData": "invoiceTotal" },
			{ "mData": "invoiceTax" },
			{ "mData": "invoiceCurrency" }
		]
	});


	$("#checkAll").click(function(){
		// alert(1);
	  if($(this).prop("checked") == true){
	      $(".editor-active").prop('checked', true);
	  } else {
	      $(".editor-active").prop('checked', false);
	  }       
	})

	$( "#fromDate" ).datepicker({
		dateFormat: 'yy-mm-dd'
	});

	$( "#toDate" ).datepicker({
		dateFormat: 'yy-mm-dd'
	});    
});
