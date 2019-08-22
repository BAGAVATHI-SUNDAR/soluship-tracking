$(document).ready( function () {
	 var table = $('#ediFilesTable').DataTable({
			"sAjaxSource": "/edi-files",
			"sAjaxDataProp": "",
			"order": [[ 2, "desc" ]],
			"aoColumns": [
			    { "mData": "name"},
			    { "mData": "carrierId" },
				{ "mData": "processedDate" },
				{ "mData": "uploadProgressCount" },
				{ "mData": "dataSynced" },
				{ "mData": "syncProgressCount" },
				{ "mData": "completed" }
			]
	 })
});