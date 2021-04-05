# Barrels of Beer
System obsługi beczek w browarze

# Endpointy
### Dodaj beczkę

```
POST /barrels/add

Request
{
    "barrelName": "Beczka #1",
    "totalCapacity": 180
}

Response 200 OK
```

### Lista beczek
```
GET /barrels

Response 200 OK
[
    {
        "id": "606a49dbd60c470a54082b57",
        "barrelName": "Beczka #1",
        "beerType": "marcowe",
        "capacity": 120,
        "totalCapacity": 180
    },
    {
        "id": "606a49e2d60c470a54082b59",
        "barrelName": "Beczka #2",
        "beerType": "Koźlak",
        "capacity": 80,
        "totalCapacity": 150
    }
]
```

### Ustaw beczkę
```
POST /barrels/{id}/set

Request
{
    "beerType": "Koźlak",
    "capacity": 80
}

Response 200 OK
```

### Sygnał z czujnika
```
GET /barrels/{id}/hit

Response 200 OK
```

### Logi
```
GET /logs

Response
[
    {
        "id": "606a49dbd60c470a54082b58",
        "barrelId": "606a49dbd60c470a54082b57",
        "barrelName": "Beczka #1",
        "beerType": "puste",
        "capacity": 0,
        "date": "2021-04-04T23:20:59.910+00:00",
        "logType": "BARREL_NEW"
    },
    {
        "id": "606a49f6d60c470a54082b5b",
        "barrelId": "606a49dbd60c470a54082b57",
        "barrelName": "Beczka #1",
        "beerType": "marcowe",
        "capacity": 120,
        "date": "2021-04-04T23:21:26.867+00:00",
        "logType": "BARREL_SET"
    },
    {
        "id": "606a4acfd60c470a54082b5d",
        "barrelId": "606a49dbd60c470a54082b57",
        "barrelName": "Beczka #1",
        "beerType": "marcowe",
        "capacity": 119,
        "date": "2021-04-04T23:25:03.605+00:00",
        "logType": "BARREL_HIT"
    }
]
```

Codzienne statystyki
```
GET /statistics/daily

Response
[
    {
        "date": "2021-04-05T00:26:34.321+00:00",
        "barrelName": "Beczka #1",
        "beerType": "Marcowe",
        "count": 8
    },
    {
        "date": "2021-04-05T00:26:34.321+00:00",
        "barrelName": "Beczka #2",
        "beerType": "Koźlak",
        "count": 5
    }
]
```