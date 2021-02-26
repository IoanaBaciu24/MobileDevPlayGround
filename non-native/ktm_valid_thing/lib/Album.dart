import 'package:ktm_valid_thing/Photo.dart';

class Album {
  String name;
  String description;
  // String path;
  List<Photo> photos;

  Album(this.name, this.description, this.photos);
}
