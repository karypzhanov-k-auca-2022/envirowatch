import 'package:envirowatch/features/dashboard/presentation/dashboard_page.dart';
import 'package:envirowatch/features/locations/presentation/pages/locations_page.dart';
import 'package:envirowatch/features/settings/presentation/pages/settings_page.dart';
import 'package:go_router/go_router.dart';


final GoRouter appRouter = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(
      path: '/',
      builder: (context, state) => const DashboardPage(),
    ),
    GoRoute(
      path: '/locations',
      builder: (context, state) => const LocationsPage(),
    ),
    GoRoute(
      path: '/settings',
      builder: (context, state) => const SettingsPage(),
    ),
  ],
);