async function init() {
    await customElements.whenDefined('gmp-map');

    const map = document.querySelector('gmp-map');
    const marker = document.querySelector('gmp-advanced-marker');
    const placePicker = document.querySelector('gmpx-place-picker');
    const infowindow = new google.maps.InfoWindow();

    let selectedLocation = null;

    map.innerMap.setOptions({
        mapTypeControl: false
    });

    placePicker.addEventListener('gmpx-placechange', () => {
        const place = placePicker.value;

        if (!place.location) {
            window.alert(
                "No details available for input: '" + place.name + "'"
            );
            infowindow.close();
            marker.position = null;
            return;
        }

        if (place.viewport) {
            map.innerMap.fitBounds(place.viewport);
        } else {
            map.center = place.location;
            map.zoom = 17;
        }

        marker.position = place.location;
        selectedLocation = place.location;

        infowindow.setContent(
            `<strong>${place.displayName}</strong><br>
            <span>${place.formattedAddress}</span>
        `);
        infowindow.open(map.innerMap, marker);
    });

    map.innerMap.addListener('click', (event) => {
        const clickedLocation = event.latLng;
        marker.position = clickedLocation;
        selectedLocation = clickedLocation;

        infowindow.setContent(
        `<strong>Odabrano mjesto:</strong><br>
        <span>Lat: ${clickedLocation.lat()}, Lng: ${clickedLocation.lng()}</span>`
        );
        infowindow.open(map.innerMap, marker);
    });

     const confirmButton = document.getElementById('potvrda-lokacije-btn');
     confirmButton.addEventListener('click', () => {
        if (selectedLocation) {

            const lat = selectedLocation.lat();
            const lng = selectedLocation.lng();
            /*
            const locationData = {
                lat: selectedLocation.lat(),
                lng: selectedLocation.lng()
            };
            localStorage.setItem('selectedLocation', JSON.stringify(locationData));
            console.log('Potvrđena lokacija:', selectedLocation);*/
            //console.log('Geografska širina:', selectedLocation.lat());
            //console.log('Geografska dužina:', selectedLocation.lng());

            const url = `http://localhost:3000/addNewConcert?lat=${lat}&lng=${lng}`;

            window.location.href = url;

            //window.location.href = 'http://localhost:3000/addNewConcert?lat=${locationData.lat}&lng=${locationData.lng}'
        } else {
            window.alert('Molimo odaberite lokaciju prije potvrde!');
        }
     });
}


document.addEventListener('DOMContentLoaded', init);