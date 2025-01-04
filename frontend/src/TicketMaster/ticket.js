document.getElementById('button-location').addEventListener('click', function() {
    const date = document.getElementById('concert-date').value;
    const artist = document.getElementById('artist-name').value;
    const radius = document.getElementById('radius').value;

    if (date) {
        localStorage.setItem('concert-date', date);
    }
    if (artist) {
        localStorage.setItem('artist-name', artist);
    }
    if (radius) {
        localStorage.setItem('radius', radius);
    }

    window.location.href = 'http://localhost:63342/zamorac/frontend/src/GoogleMaps/GoogleMaps.html?_ijt=ml0hnlo0ra6317f2o6s3o373bo&_ij_reload=RELOAD_ON_SAVE';
});

window.addEventListener('load', function() {
    const savedDate = localStorage.getItem('concert-date');
    const savedArtist = localStorage.getItem('artist-name');
    const savedRadius = localStorage.getItem('radius');
    if (savedDate) {
        document.getElementById('concert-date').value = savedDate;
    }

    if (savedArtist) {
        document.getElementById('artist-name').value = savedArtist;
    }

    if (savedRadius) {
        document.getElementById('radius').value = savedRadius;
    }

    const savedLocation = localStorage.getItem('selectedLocation');
    if (savedLocation) {
        const locationData = JSON.parse(savedLocation);
        const locationText = `Lat: ${locationData.lat}, Lng: ${locationData.lng}`;
        document.getElementById('location').value = locationText;
    }
});

document.getElementById('button-clear-location').addEventListener('click', function() {
    document.getElementById('location').value = '';
});

document.getElementById('parameters-form').addEventListener('submit', function(event) {
    event.preventDefault();

    const date = document.getElementById('concert-date').value;
    const artist = document.getElementById('artist-name').value;
    const location = document.getElementById('location').value;
    const radius = document.getElementById('radius').value;

    let query = '';

    if (radius && !location) {
        alert('Please select a location before entering a radius.');
        return;
    }

    if (date) {
        query += `?date=${date}`;
    }

    if (artist) {
        query += (query ? '&' : '?') + `artist=${artist}`;
    }

    if (location) {
        const [latPart, lngPart] = location.split(',');
        const lat = latPart.split(':')[1].trim();
        const lng = lngPart.split(':')[1].trim();
        query += (query ? '&' : '?') + `latitude=${lat}&longitude=${lng}`;
    }

     if (location && radius) {
        query += (query ? '&' : '?') + `radius=${radius}`;
     }

    //console.log(`Final API request URL: http://localhost:8080/concerts/concerts${query}`);

    fetch(`http://localhost:8080/concerts/concerts${query}`, { method: 'GET' })
        .then(response => {
            if (!response.ok) {
                throw new Error('No concerts match your search criteria. Please try different parameters.');
            }
            return response.json();
        })
        .then(data => {
            console.log(data); //podaci o koncertima u DATA!
            if (data.length === 0) {
                alert("No concerts match your search criteria. Please try different parameters.");
            } else {
                localStorage.setItem('concerts', JSON.stringify(data));
                window.open('http://localhost:63342/zamorac/frontend/src/ConcertDetails/concertsResults.html?_ijt=c8u86g04tnip2cvvamban0k8k7&_ij_reload=RELOAD_ON_SAVE', '_blank');
            }
        });
});