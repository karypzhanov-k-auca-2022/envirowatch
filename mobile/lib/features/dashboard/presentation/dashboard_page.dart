import 'package:envirowatch/core/router/app_router.dart';
import 'package:flutter/material.dart';

class DashboardPage extends StatelessWidget {
  const DashboardPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Text('Dashboard Page'),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: () => appRouter.go('/localation'),
              child: const Text('Go to Locations'),
            ),
          ],
        ),
      ),
    );
  }
}
