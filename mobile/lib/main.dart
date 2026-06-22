import 'package:envirowatch/core/di/injection_container.dart';
import 'package:envirowatch/core/router/app_router.dart';
import 'package:flutter/material.dart';

void main() async {
  // для асинхронных вызовов перед runApp
  WidgetsFlutterBinding.ensureInitialized(); 
  
  // Инициализируем зависимости
  await init(); 

  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'EnviroWatch',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.blue),
        useMaterial3: true,
      ),
      routerConfig: appRouter,
    );
  }
}