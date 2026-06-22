import 'package:envirowatch/core/network/api_constants.dart';
import 'package:get_it/get_it.dart';
import 'package:dio/dio.dart';

final sl = GetIt.instance; // sl = Service Locator

Future<void> init() async {
  // 1. External (Dio, SharedPreferences)
  sl.registerLazySingleton(() => Dio());

  // 2. Repository / Data Sources (будут позже)
  // 3. BLoC / Cubit (будут позже)
  sl.registerLazySingleton(
    () => Dio(
      BaseOptions(
        baseUrl: ApiConstants.baseUrl,
        connectTimeout: const Duration(seconds: 5),
        receiveTimeout: const Duration(seconds: 3),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
      ),
    ),
  );
}
