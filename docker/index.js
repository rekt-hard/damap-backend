//how to run: execute the dummy api with node index.js or nodemon index.js

const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");

const app = express();
const PORT = process.env.PORT || 8000;

app.use(cors());
app.use(bodyParser.json());

const projectlist = [
  {
    "id": 159138,
    "title": "Project title 1",
    "acronym": "Acronym 1",
    "description": "Project description 1",
    "start": "31-12-2018",
    "end": "29-06-2021",
    "funding": {
      "fundingName": "FFG",
      "fundingProgram": "FFG",
      "funderId": {
        "identifier": "501100000780",
        "type": "FUNDREF"
      },
      "grantId": {
        "identifier": "831644",
        "type": "FUNDREF"
      },
      "fundingStatus": "GRANTED"
    },
    "orgUnitList": null,
    "projectMembers": null
  },
  {
    "id": 134379,
    "acronym": "Acronym 2",
    "description": "Project description 2",
    "title": "Project 2",
    "start": "31-10-2018",
    "end": "13-10-2021",
    "funding": {
      "fundingName": "FWF",
      "fundingProgram": "FWF",
      "funderId": {
        "identifier": "501100000780",
        "type": "FUNDREF"
      },
      "grantId": {
        "identifier": "573700-EPP-1-2016-1-PS-EPPKA2-CBHE-JP (2016-2540)",
        "type": null
      },
      "fundingStatus": "GRANTED"
    },
    "orgUnitList": null,
    "projectMembers": null
  }
];

const projectapi1 = {
  "id": 159138,
  "acronym": "Acronym 1",
  "description": null,
  "title": "Project title 1",
  "start": "31-12-2018",
  "end": "29-06-2021",
  "funding": {
    "fundingName": "FFG",
    "fundingProgram": "FFG",
    "funderId": {
      "identifier": "501100000780",
      "type": "FUNDREF"
    },
    "grantId": {
      "identifier": "831644",
      "type": null
    },
    "fundingStatus": "GRANTED"
  },
  "orgUnitList": [
    {
      "id": 1000100,
      "code": "E194-01"
    },
    {
      "id": 1000933,
      "code": "E058-06"
    },
    {
      "id": 1000941,
      "code": "E040-04"
    }
  ],
  "projectMembers": [
    {
      "id": 12345,
      "role": "Project leader",
      "projectLeader": true
    },
    {
      "id": 23456,
      "role": "Project manager",
      "projectLeader": false
    }
  ]
};

const projectapi2 = {
  "id": 134379,
  "acronym": "Acronym 2",
  "description": null,
  "title": "Project title 2",
  "start": "31-12-2018",
  "end": "29-06-2021",
  "funding": {
    "fundingName": "FWF",
    "fundingProgram": "FWF",
    "funderId": {
      "identifier": "501100000780",
      "type": "FUNDREF"
    },
    "grantId": {
      "identifier": "831644",
      "type": null
    },
    "fundingStatus": "GRANTED"
  },
  "orgUnitList": [
    {
      "id": 1000100,
      "code": "E194-01"
    },
    {
      "id": 1000933,
      "code": "E058-06"
    },
    {
      "id": 1000941,
      "code": "E040-04"
    }
  ],
  "projectMembers": [
    {
      "id": 39608,
      "role": "Project leader",
      "projectLeader": true
    },
    {
      "id": 12345,
      "role": "Project manager",
      "projectLeader": false
    },
    {
      "id": 23456,
      "role": "Project manager",
      "projectLeader": false
    }
  ]
};

const personapi1 = {
  "tiss_id": 12345,
  "oid": 1026725,
  "old_tiss_ids": [],
  "first_name": "First",
  "last_name": "Last",
  "gender": "F",
  "pseudoperson": false,
  "preceding_titles": "Dipl.-Ing. Dr.techn.",
  "postpositioned_titles": null,
  "orcid": 123,
  "card_uri": "/person/39608",
  "picture_uri": "/illustration/anzeigen/f8fd10798a3a.jpeg?v1",
  "main_phone_number": "+43 1 58801 18826",
  "main_email": "email@university.at",
  "other_emails": [
    "email@university.at"
  ],
  "employee": [
    {
      "org_ref": {
        "tiss_id": 4758,
        "code": "E194-01",
        "number": "E194-01",
        "name_de": "Forschungsbereich Information und Software Engineering",
        "name_en": "Research Unit of Information and Software Engineering",
        "type": "FOB"
      },
      "internal_code": null,
      "function": "Head",
      "function_category": "Head",
      "room": {
        "room_code": "HG0105",
        "address": {
          "street": "Favoritenstraße 11",
          "zip_code": "1040",
          "city": "Wien",
          "country": "AT",
          "co": null
        }
      },
      "websites": [
        {
          "uri": "http://www.website.com",
          "title": "http://www.website.com"
        }
      ]
    }
  ]
};

const personapi2 = {
  "tiss_id": 23456,
  "oid": 1026725,
  "old_tiss_ids": [],
  "first_name": "Alex",
  "last_name": "Ferguson",
  "gender": "M",
  "pseudoperson": false,
  "preceding_titles": null,
  "postpositioned_titles": null,
  "orcid": 234,
  "card_uri": "/person/39608",
  "picture_uri": null,
  "main_phone_number": "+43 1 58801 18826",
  "main_email": "email@university.at",
  "other_emails": [
    "email@university.at"
  ],
  "employee": [
    {
      "org_ref": {
        "tiss_id": 4758,
        "code": "E194-01",
        "number": "E194-01",
        "name_de": "Forschungsbereich Information und Software Engineering",
        "name_en": "Research Unit of Information and Software Engineering",
        "type": "FOB"
      },
      "internal_code": null,
      "function": "Head",
      "function_category": "Head",
      "room": {
        "room_code": "HG0105",
        "address": {
          "street": "Favoritenstraße 11",
          "zip_code": "1040",
          "city": "Wien",
          "country": "AT",
          "co": null
        }
      },
      "websites": [
        {
          "uri": "http://www.website.com",
          "title": "http://www.website.com"
        }
      ]
    }
  ]
};

const personapi3 = {
  "tiss_id": 39608,
  "oid": 1026725,
  "old_tiss_ids": [],
  "first_name": "Sarah",
  "last_name": "Sarah",
  "gender": "F",
  "pseudoperson": false,
  "preceding_titles": "Dipl.-Ing. Dr.techn.",
  "postpositioned_titles": null,
  "orcid": 345,
  "card_uri": "/person/34567",
  "picture_uri": null,
  "main_phone_number": "+43 1 58801 18826",
  "main_email": "email@university.at",
  "other_emails": [
    "email@university.at"
  ],
  "employee": [
    {
      "org_ref": {
        "tiss_id": 4758,
        "code": "E194-01",
        "number": "E194-01",
        "name_de": "Forschungsbereich Information und Software Engineering",
        "name_en": "Research Unit of Information and Software Engineering",
        "type": "FOB"
      },
      "internal_code": null,
      "function": "Head",
      "function_category": "Head",
      "room": {
        "room_code": "HG0105",
        "address": {
          "street": "Favoritenstraße 11",
          "zip_code": "1040",
          "city": "Wien",
          "country": "AT",
          "co": null
        }
      },
      "websites": [
        {
          "uri": "http://www.website.com",
          "title": "http://www.website.com"
        }
      ]
    }
  ]
};

const projectquest1 = {
  "personalData":false,
  "dualUse":false,
  "nonEUCountry":false,
  "dataManagementCost":false
};

const projectquest2 = {
  "personalData":false,
  "dualUse":false,
  "nonEUCountry":false,
  "dataManagementCost":false
};

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});

app.get("/api/pdb/rest/restricted/damap/projectsByCriteria", (req, res) => {

  res.send(projectlist);

});

app.get("/api/person/v22/id/12345", (req, res) => {

  res.send(personapi1);

});

app.get("/api/person/v22/id/23456", (req, res) => {

  res.send(personapi2);

});

app.get("/api/person/v22/id/39608", (req, res) => {

  res.send(personapi3);

});

app.get("/api/pdb/rest/restricted/damap/project/159138", (req, res) => {

  res.send(projectapi1);

});

app.get("/api/pdb/rest/restricted/damap/project/134379", (req, res) => {

  res.send(projectapi2);

});

app.get("/api/pdb/rest/restricted/damap/projectQuestionnaire/159138", (req, res) => {

  res.send(projectquest1);

});

app.get("/api/pdb/rest/restricted/damap/projectQuestionnaire/134379", (req, res) => {

  res.send(projectquest2);

});