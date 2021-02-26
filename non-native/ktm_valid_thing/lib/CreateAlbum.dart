import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:ktm_valid_thing/Album.dart';
import 'package:ktm_valid_thing/Photo.dart';
import 'package:ktm_valid_thing/Repository.dart';
import 'package:ktm_valid_thing/main.dart';

class CreateAlbum extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => CreateAlbumState();
}

class CreateAlbumState extends State<CreateAlbum> {
  final nameController = TextEditingController();
  final descriptionController = TextEditingController();
  final Repository repository = Repository();
  @override
  void dispose() {
    // Clean up the controller when the widget is disposed.
    nameController.dispose();
    descriptionController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      body: Center(
          child: Column(
        children: [
          TextField(controller: nameController),
          TextField(
            controller: descriptionController,
          )
        ],
      )),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          setState(() {
            repository.items.add(Album(nameController.text,
                descriptionController.text, List<Photo>()));
            print(repository.items);
          });
          Navigator.pop(context);
        },
      ),
    );
  }
}
