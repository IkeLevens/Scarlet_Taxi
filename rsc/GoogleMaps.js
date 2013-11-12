APIKEY = 'IzaSyDgDwohM6V-iqpv76Llg5qgGlHnO_wj6A4'

var geocoder;
var map;

function viewPickUpMap() {
    var address = $('#Address').val();
    if ( $('#Address').val() === "") {
        console.log('Empty form');
    } else {
        console.log('Form with something in it');
        codeAddress();
    }
    return;
}

function codeAddress() {
    var address = $('#Address').val();
    geocoder.geocode( { 'address': address}, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            map.setCenter(results[0].geometry.location);
                var marker = new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location
                });
        } else {
            alert('Geocode was not successful for the following reason: ' + status);
        }
    });
}
