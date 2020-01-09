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


// Charity Endpoints //
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

app.post('/v0/charity/createDonationAccount', function (req, res) {
    db.ref('/users/' + req.query.uid).once('value').then(function(snapshot) {
        if(snapshot.exists()) {
            res.send(JSON.parse('{"error": "Account already has donation account"}'))
        } else {
            var options = { 
                method: 'POST',
                url: 'http://api.reimaginebanking.com/customers/5e175612322fa016762f37da/accounts',
                qs: { key: 'fb6115503f4e5c0d96debdf0fb760ec3' },
                body: { 
                    type: 'Savings',
                    nickname: 'Savings account towards charities',
                    rewards: 0,
                    balance: 0 
                },
                json: true 
            };
            request(options, function (error, response, body) {
            if (error) throw new Error(error);
            admin.database().ref('/users/' + req.query.uid).update({
                doesHaveDonationAccount: true
            })
            res.send(body)
            });
        }
    })
})

app.listen(port, () => console.log(`Trash State School Coder server running on: ${port}!`))

// db.ref('/').once('value').then(function(snapshot) {
//     console.log(snapshot.val())
// })