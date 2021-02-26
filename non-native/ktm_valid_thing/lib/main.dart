import 'dart:io';
import 'package:ktm_valid_thing/CreateAlbum.dart';
import 'package:ktm_valid_thing/LifeCycleEventHandler.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:flutter/material.dart';
import 'package:ktm_valid_thing/AlbumDetail.dart';
import 'package:ktm_valid_thing/Repository.dart';

void main() {
  runApp(MaterialApp(
    home: MyApp(),
  ));
}

Repository repo = Repository();

class MyApp extends StatefulWidget {
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> with WidgetsBindingObserver {
  PermissionStatus _status;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addObserver(this);
    // WidgetsBinding.instance.addObserver(LifecycleEventHandler(
    //     resumeCallBack: () async => setState(() {
    //           print("alo");
    //         })));
    (context as Element).reassemble();
  }

  @override
  void didChangeAppLifecycleState(AppLifecycleState state) {
    if (state == AppLifecycleState.resumed) {
      print("alo");
      setState(() {});
    }
  }

  @override
  Widget build(BuildContext context) {
    PermissionHandler().requestPermissions([
      PermissionGroup.photos,
      PermissionGroup.camera,
      PermissionGroup.storage
    ]).then((value) => print(value));
    return Scaffold(
        body: Container(
            width: 1500.0,
            height: 1500.0,
            child: Stack(children: [
              Container(
                  child: Text('Keep track\nof your memories\n\n',
                      overflow: TextOverflow.ellipsis,
                      // style: TextStyle(fontWeight: FontWeight.bold),
                      style: DefaultTextStyle.of(context)
                          .style
                          .apply(fontSizeFactor: 0.7, fontWeightDelta: 2))),
              Container(
                alignment: Alignment.lerp(
                    Alignment.topRight, Alignment.centerRight, 0.5),
                child: ButtonTheme(
                  minWidth: 85.0,
                  height: 75.0,
                  buttonColor: Colors.deepPurple,
                  child: RaisedButton(
                    onPressed: () {
                      Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => CreateAlbum()))
                          .then((value) {
                        (context as Element).reassemble();

                        setState(() {});
                      });
                    },
                    child: Text("Create new album"),
                  ),
                ),
              ),
              Container(
                  alignment: Alignment.lerp(
                      Alignment.centerLeft, Alignment.bottomLeft, 0.7),
                  child: ListView.builder(
                      itemCount: repo.items.length,
                      shrinkWrap: true,
                      scrollDirection: Axis.vertical,
                      itemBuilder: (context, index) {
                        final List<ListItem> items =
                            List<ListItem>.generate(repo.items.length, (index) {
                          if (index >= repo.items.length) {
                            return AlbumItem("", "");
                          }
                          String path;
                          if (repo.items[index].photos.length == 0) {
                            path = "/storage/emulated/0/Download/3460797.png";
                          } else {
                            path = repo.items[index].photos[0].path;
                          }
                          return AlbumItem(repo.items[index].name, path);
                        });
                        final item = items[index];
                        print(repo.items);
                        return ListView(
                          shrinkWrap: true,
                          children: [
                            ListTile(
                              title: item.buildTitle(context),
                              subtitle: item.buildName(context),
                              onTap: () {
                                Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => AlbumDetail(
                                              album: repo.items[index],
                                            ))).then((value) {
                                  print(repo.items.length);
                                  setState(() {});
                                  (context as Element).rebuild();
                                });
                              },
                            )
                          ],
                        );
                      })),
            ])));
  }
}

abstract class ListItem {
  Widget buildName(BuildContext context);
  Widget buildTitle(BuildContext context);
}

class AlbumItem implements ListItem {
  final String name;
  final String coverPhotoPath;
  // final Image image

  @override
  Widget buildName(BuildContext context) => Stack(children: [
        //  Image.file(File(coverPhotoPath))
        Container(
            alignment: Alignment.bottomCenter,
            child: Image.file(File(coverPhotoPath))),
      ]);

  Widget buildTitle(BuildContext context) => Text(name);

  AlbumItem(this.name, this.coverPhotoPath);
}
