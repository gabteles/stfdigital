/*
*  Altair Admin
*  @version v1.3.0
*  @author tzd
*  @license http://themeforest.net/licenses
*  plugins_google_maps.js - plugins_google_maps.html
*/

// google maps variables
var marker_url = isHighDensity() ? 'assets/img/md-images/ic_place_black_48dp.png' : 'assets/img/md-images/ic_place_black_24dp.png',
    marker_url_beenhere = isHighDensity() ? 'assets/img/md-images/ic_beenhere_black_48dp' : 'assets/img/md-images/ic_beenhere_black_24dp',
    marker_url_history = isHighDensity() ? 'assets/img/md-images/ic_location_history_black_48dp.png' : 'assets/img/md-images/ic_location_history_black_24dp.png',
    marker_url_dining = isHighDensity() ? 'assets/img/md-images/ic_local_dining_black_48dp.png' : 'assets/img/md-images/ic_local_dining_black_24dp.png',
    marker_size = isHighDensity() ? new google.maps.Size(48, 48) : new google.maps.Size(24, 24),
    marker_scaled_size = new google.maps.Size(24, 24),
    marker_zoom = 14,
    LocsA = [
        {
            lat: 45.9,
            lon: 10.9,
            title: 'Title A',
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Heading A</h3>' +
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join(),
            icon: {
                url: marker_url_beenhere,
                size: marker_size,
                scaledSize: marker_scaled_size
            }
        },
        {
            lat: 44.8,
            lon: 1.7,
            title: 'Title B',
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Heading B</h3>' +
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join(''),
            icon: {
                url: marker_url_history,
                size: marker_size,
                scaledSize: marker_scaled_size
            }
        },
        {
            lat: 51.5,
            lon: -1.1,
            title: 'Title C',
            icon: {
                url: marker_url,
                size: marker_size,
                scaledSize: marker_scaled_size
            },
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Heading C</h3>',
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join('')
        }
    ],
    LocsB = [
        {
            lat: 45.4654,
            lon: 9.1866,
            title: 'Milan, Italy',
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Milan, Italy</h3>',
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join('')
        },
        {
            lat: 47.36854,
            lon: 8.53910,
            title: 'Zurich, Switzerland',
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Zurich, Switzerland</h3>',
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join('')
        },
        {
            lat: 48.892,
            lon: 2.359,
            title: 'Paris, France',
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Paris, France</h3>',
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join('')
        },
        {
            lat: 48.13654,
            lon: 11.57706,
            title: 'Munich, Germany',
            html: [
                '<div class="gmap-info-window">' +
                '<h3 class="uk-text-nowrap">Munich, Germany</h3>',
                '<p>Lorem ipsum dolor sit&hellip;</p>' +
                '</div>'
            ].join('')
        }
    ],
    map_style_a = {
        'Neutral Blue': [
            {
                "featureType": "water",
                "elementType": "geometry",
                "stylers": [{"color": "#193341"}]
            }, {
                "featureType": "landscape",
                "elementType": "geometry",
                "stylers": [{"color": "#2c5a71"}]
            }, {
                "featureType": "road",
                "elementType": "geometry",
                "stylers": [{"color": "#29768a"}, {"lightness": -37}]
            }, {
                "featureType": "poi", "elementType": "geometry", "stylers": [{"color": "#406d80"}]}, {
                "featureType": "transit",
                "elementType": "geometry",
                "stylers": [{"color": "#406d80"}]
            }, {
                "elementType": "labels.text.stroke",
                "stylers": [{"visibility": "on"}, {"color": "#3e606f"}, {"weight": 2}, {"gamma": 0.84}]
            }, {
                "elementType": "labels.text.fill", "stylers": [{"color": "#ffffff"}]}, {
                "featureType": "administrative",
                "elementType": "geometry",
                "stylers": [{"weight": 0.6}, {"color": "#1a3541"}]
            }, {
                "elementType": "labels.icon", "stylers": [{"visibility": "off"}]}, {
                "featureType": "poi.park",
                "elementType": "geometry",
                "stylers": [{"color": "#2c5a71"}]
            }
        ]
    },
    map_style_b = {
        'Subtle Greyscale': [{
            "featureType": "landscape",
            "stylers": [{"saturation": -100}, {"lightness": 65}, {"visibility": "on"}]
        }, {
            "featureType": "poi",
            "stylers": [{"saturation": -100}, {"lightness": 51}, {"visibility": "simplified"}]
        }, {
            "featureType": "road.highway",
            "stylers": [{"saturation": -100}, {"visibility": "simplified"}]
        }, {
            "featureType": "road.arterial",
            "stylers": [{"saturation": -100}, {"lightness": 30}, {"visibility": "on"}]
        }, {
            "featureType": "road.local",
            "stylers": [{"saturation": -100}, {"lightness": 40}, {"visibility": "on"}]
        }, {
            "featureType": "transit",
            "stylers": [{"saturation": -100}, {"visibility": "simplified"}]
        }, {
            "featureType": "administrative.province", "stylers": [{"visibility": "off"}]}, {
            "featureType": "water",
            "elementType": "labels",
            "stylers": [{"visibility": "on"}, {"lightness": -25}, {"saturation": -100}]
        }, {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [{"hue": "#ffff00"}, {"lightness": -25}, {"saturation": -97}]
        }]
    };

$(function() {
    // google maps
    altair_gmaps.gm_markers();
    altair_gmaps.gm_route();
    altair_gmaps.style_a();
    altair_gmaps.style_b();
});

altair_gmaps = {
    gm_markers: function() {
        new Maplace({
            locations: LocsA,
            map_div: '#gmap_markers',
            controls_title: 'Choose a location:',
            listeners: {
                idle: function() {
                    var mapDropown = $('#gmap_markers').find('.dropdown.gmap_controls').find('select');
                    if(mapDropown && !mapDropown.hasClass('data-md-selectize')) {
                        mapDropown.addClass('data-md-selectize');
                        altair_forms.select_elements();
                    }
                }
            }
        }).Load();

    },
    gm_route: function() {
        new Maplace({
            locations: LocsB,
            map_div: '#gmap_route',
            generate_controls: false,
            show_markers: false,
            type: 'directions',
            draggable: true,
            directions_panel: '#route_directions',
            afterRoute: function(distance) {
                $('.route_km').text('('+(distance/1000)+'km)');
            }
        }).Load();
    },
    style_a: function() {
        new Maplace({
            map_div: '#gmap_style_a',
            map_options: {
                set_center: [37.390267, -122.076417],
                zoom: 12,
                streetViewControl: false
            },
            styles: map_style_a,
            controls_on_map: false
        }).Load();
    },
    style_b: function() {
        new Maplace({
            map_div: '#gmap_style_b',
            map_options: {
                set_center: [37.390267, -122.076417],
                zoom: 12,
                streetViewControl: false
            },
            draggable: true,
            styles: map_style_b
        }).Load();
    }
};
