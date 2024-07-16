package com.philimonnag.smarttaskmanager.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


import android.location.Geocoder
import androidx.compose.material.Button
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*
val indiaState = LatLng(
    20.5937, 78.9629
)
val defaultCameraPosition = CameraPosition.fromLatLngZoom(indiaState, 4f)
@Composable
fun GoogleMapScreen(navController: NavController, latitude: Double?, longitude: Double?) {
    val cameraPositionState = rememberCameraPositionState {
        position = defaultCameraPosition
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    val initialPosition = if (latitude != null && longitude != null) {
        LatLng(latitude, longitude)
    } else {
        indiaState
    }

    val locationState = rememberMarkerState(position = initialPosition)
    var markerTitle by remember { mutableStateOf("Initial Location") }

    if (latitude != null && longitude != null) {
        markerTitle = GetPlaceNameFromLatLng(latitude, longitude)
    }

//    LaunchedEffect(locationState.position) {
//        markerTitle = GetPlaceNameFromLatLng(
//            locationState.position.latitude,
//            locationState.position.longitude)
//    }

    Box(Modifier.fillMaxSize()) {
        Column {
            GoogleMapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    isMapLoaded = true
                },
                locationState = locationState,
                markerTitle = markerTitle,
            )
            if (!isMapLoaded) {
                AnimatedVisibility(visible = !isMapLoaded) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(
                                Alignment.CenterHorizontally
                            )
                    )
                }
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("lat", locationState.position.latitude)
                    navController.previousBackStackEntry?.savedStateHandle?.set("long", locationState.position.longitude)
                    navController.previousBackStackEntry?.savedStateHandle?.set("title", markerTitle)
                    navController.popBackStack()
                }
            ) {
                Text("Save Location")
            }
        }
    }
}
@SuppressLint("MissingPermission")
@Composable
fun GoogleMapView(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    onMapLoaded: () -> Unit,
    locationState: MarkerState,
    markerTitle: String,
) {
    val mapUiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    val mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL)) }

    GoogleMap(
        modifier = modifier,
        onMapLoaded = onMapLoaded,
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties
    ) {
        Marker(
            state = locationState,
            draggable = true,
            title = markerTitle,
            contentDescription = "Location",

        )
    }
}



@Composable
fun GetPlaceNameFromLatLng(latitude: Double, longitude: Double): String {
    val context = LocalContext.current
    var placeName by remember { mutableStateOf("Unknown Location") }

    LaunchedEffect(latitude, longitude) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    placeName = addresses[0].getAddressLine(0) // or other fields like getLocality(), getCountryName(), etc.
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    return placeName
}


//@Preview(showBackground = true)
//@Composable
//fun GoogleMapScreen() {
//    val cameraPositionState = rememberCameraPositionState {
//        position = defaultCameraPosition
//    }
//
//    var isMapLoaded by remember {
//        mutableStateOf(false)
//    }
//
//    Box(Modifier.fillMaxSize()) {
//        GoogleMapView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(450.dp),
//            cameraPositionState = cameraPositionState,
//            onMapLoaded = {
//                isMapLoaded = true
//            }
//        )
//        if (!isMapLoaded) {
//            AnimatedVisibility(visible = !isMapLoaded) {
//                CircularProgressIndicator(
//                    modifier = Modifier
//                        .wrapContentSize()
//                        .align(
//                            Alignment.Center
//                        )
//                )
//            }
//
//        }
//
//    }
//}
//
//
//@SuppressLint("MissingPermission")
//@Composable
//fun GoogleMapView(
//    modifier: Modifier = Modifier,
//    cameraPositionState: CameraPositionState,
//    onMapLoaded: () -> Unit
//) {
//
//    val locationState = rememberMarkerState(
//        position = indiaState
//    )
//
//    val mapUiSettings by remember {
//        mutableStateOf(MapUiSettings(compassEnabled = false))
//    }
//
//    val mapProperties by remember {
//        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
//    }
//
//    var showInfoWindow by remember {
//        mutableStateOf(true)
//    }
//
//    GoogleMap(
//        modifier = modifier,
//        onMapLoaded = onMapLoaded,
//        cameraPositionState = cameraPositionState,
//        uiSettings = mapUiSettings,
//        properties = mapProperties
//    ) {
//
//
//        Marker(
//            state = locationState,
//            draggable = true,
//            title = "Philimon",
//            contentDescription = "Location"
//        )
////        MarkerInfoWindowContent(
////            state = locationState,
////            draggable = true,
////            onClick = {
////                if (showInfoWindow) {
////                    locationState.showInfoWindow()
////                } else {
////                    locationState.hideInfoWindow()
////                }
////                showInfoWindow = !showInfoWindow
////                return@MarkerInfoWindowContent false
////            },
////            title = "India Map Title"
////        ) {
////            Column {
////                Text(text = "CodingAmbitions From India")
////                Text(text = "CodingAmbitions From India")
////                Text(text = "CodingAmbitions From India")
////
////            }
////        }
//
//    }
//
//}