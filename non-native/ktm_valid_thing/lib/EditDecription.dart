import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:ktm_valid_thing/Repository.dart';

import 'Album.dart';

class EditDescription extends StatefulWidget {
  Album album;
  EditDescription({Key key, @required this.album}) : super(key: key);

  @override
  State<StatefulWidget> createState() => EditDescriptionState(album);
}

class EditDescriptionState extends State<EditDescription> {
  Album album;
  EditDescriptionState(this.album);
  TextEditingController descrController = TextEditingController();

  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    descrController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
        children: [
          Container(
            alignment: Alignment.center,
            child: TextField(controller: descrController),
          ),
          Container(
            alignment: Alignment.bottomCenter,
            child: ButtonTheme(
              minWidth: 85.0,
              height: 75.0,
              buttonColor: Colors.deepPurple,
              child: RaisedButton(
                onPressed: () {
                  showAlertDialog(context, album, descrController.text);
                },
                child: Text("Edit Description"),
              ),
            ),
          )
        ],
      ),
    );
  }
}

showAlertDialog(BuildContext context, Album album, String description) {
  // set up the buttons
  Widget cancelButton = FlatButton(
    child: Text("Cancel"),
    onPressed: () {},
  );
  Widget continueButton = FlatButton(
    child: Text("Continue"),
    onPressed: () {
      updateAlbumDescription(album, description);
    },
  );
  // set up the AlertDialog
  AlertDialog alert = AlertDialog(
    title: Text("AlertDialog"),
    content: Text("Are you sure you want to delete this album?"),
    actions: [
      cancelButton,
      continueButton,
    ],
  );
  // show the dialog
  showDialog(
    context: context,
    builder: (BuildContext context) {
      return alert;
    },
  );
}

void updateAlbumDescription(Album album, String description) {
  Repository repository = Repository();
  repository.updateAlbumDesc(album, description);
}
