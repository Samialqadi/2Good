require('dotenv').config()
const express = require('express')
const app = express()
const port = process.env.PORT
const request = require('request');
var admin = require("firebase-admin");
var serviceAccount = require('./service-account.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://capital-one-19531.firebaseio.com"
});

var db = admin.database();


// Charity Endpoints
app.get('/v0/charity/getCharities', function (req, res) {
    var options = { method: 'GET',
    url: 'https://api.data.charitynavigator.org/v2/Organizations',
    qs: 
     { app_id: process.env.CHARITY_APP_ID,
       app_key: process.env.CHARITY_APP_KEY,
       pageSize: 5,
       rated: true,
       sort:"RATING:DESC"
     }
    };
    request(options, function (error, response, body) {
    if (error) throw new Error(error);  
    res.send(JSON.parse(body))
  });
})

app.get('/v0/geofence/getPlaces', function (req, res) {
  const type = req.query.type || "";
  const location = req.query.location || "";

  if (type == "" || location == "") {
    return res.error("Not all parameters provided");
  }

  var options = { method: 'GET',
  url: 'https://maps.googleapis.com/maps/api/place/nearbysearch/json',
  qs: 
   { 
     key: process.env.GOOGLE_MAPS_KEY,
     radius: 5000,
     location: location,
     type: type
   }
  };
  request(options, function (error, response, body) {
    if (error) throw new Error(error); 

    let locations = [];
    const results = JSON.parse(body)['results'];

    for (let i = 0; i < results.length; ++i) {
      locations.push({
        lat: results[i]['geometry']['location']['lat'],
        lng: results[i]['geometry']['location']['lng']
      })
    }
    
    return res.send({ locations: locations });
});
})

app.listen(port, () => console.log(`Trash State School Coder server running on: ${port}!`))

// db.ref('/').once('value').then(function(snapshot) {
//     console.log(snapshot.val())
// })