# Fachschaft07 App (for Android)
The official Android App (https://play.google.com/store/apps/details?id=com.fk07) of the Faculty 07 of the University of applied Science Munich. The app is developed by students from the Student Council.


![Preview](https://github.com/Fachschaft07/fs-app/blob/dev/screencaptures/Fachschaft_Preview_Banner.png)

## Project Architecture
The project Architecture contains two main components, the UI and Datastore.

### UI
The UI contains and handles all the frontend stuff. So when the user interacts with the App, the backend is notified to load some data in an async task, which will be notify the frontend if the data is loaded and can be displayed.

The interfaces to communicate with the backend are the XYZHelper classes. They can be found in the package [edu.hm.cs.fs.app.datastore.helper](https://github.com/Fachschaft07/fs-app/tree/dev/app/src/main/java/edu/hm/cs/fs/app/datastore/helper).

### Datastore
The datastore itself has 3 components:

* **Web**: The data which will be retrieved from the internet is fetched by the [XYZFetcher](https://github.com/Fachschaft07/fs-app/tree/dev/app/src/main/java/edu/hm/cs/fs/app/datastore/web) classes. Their parse the content (xml, json, html,...) and return an Model Object.
* **Model**: The [models](https://github.com/Fachschaft07/fs-app/tree/dev/app/src/main/java/edu/hm/cs/fs/app/datastore/model/impl) are defined in component. They only have some simple Getter- and Setter-Methods. The model will never be returned to the UI!
* **Helper**: The [XYZHelper](https://github.com/Fachschaft07/fs-app/tree/dev/app/src/main/java/edu/hm/cs/fs/app/datastore/helper) classes implement an [interface](https://github.com/Fachschaft07/fs-app/tree/dev/app/src/main/java/edu/hm/cs/fs/app/datastore/model) which will define the methods which will be accessible by the frontend. The helper classes also do the split between the web fetchers and the local database. If the data was not updated, then the database is called, otherwise the web fetchers.

## UI-Design
The design of the app is modeled on the [Material Design](https://www.google.com/design/spec/material-design/introduction.html) of Google.

## License

    Copyright 2015 Student Council of the Faculty 07 of the University of applied Science Munich

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
