package com.goldenhour.util.map

import android.content.Context
import com.goldenhour.R
import com.goldenhour.util.HttpRequester
import com.goldenhour.util.info
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.util.*
import javax.inject.Singleton

/**
 * Singleton class for common utility method for Map
 */
@Singleton
object MapUtils {
    private val TAG = MapUtils::class.java.name

    /**
     * Getting URL From Geocoding API
     */
    private fun getGAPIAddressUrl(context: Context, latLng: LatLng): String {
        return ("https://maps.googleapis.com/maps/api/geocode/json?"
                + "latlng="
                + latLng.latitude
                + ","
                + latLng.longitude
                + "&sensor=true"
                + "&key="
                + context.getString(R.string.google_maps_key))
    }

    /**
     * Getting address from latlng using Geocoding API
     */
    fun getGAPIAddress(context: Context, latLng: LatLng): String {
        var fullAddress = StringBuilder()
        try {
            val url = getGAPIAddressUrl(context, latLng)
            info(TAG, "URL :: $url")
            val jsonObj = JSONObject(
                HttpRequester().getJSONFromUrl(url)
            )

            info(TAG, "json object=$jsonObj\n Url :: $url")
            val status = jsonObj.getString("status")
            if (status.equals("OK", ignoreCase = true)) {
                val results = jsonObj.getJSONArray("results")
                val zero = results.getJSONObject(0)

                var streetNumber = ""
                var route = ""
                var subLocality2 = ""
                var subLocality1 = ""
                var locality = ""
                var administrativeArea2 = ""
                var administrativeArea1 = ""
                var country = ""
                var postalCode = ""

                if (zero.has("address_components")) {
                    try {

                        val selectedAddressComponentsArr = ArrayList<String>()
                        val addressComponents = zero.getJSONArray("address_components")

                        for (i in 0 until addressComponents.length()) {

                            val iObj = addressComponents.getJSONObject(i)
                            val jArr = iObj.getJSONArray("types")

                            val addressTypes = ArrayList<String>()
                            for (j in 0 until jArr.length()) {
                                addressTypes.add(jArr.getString(j))
                            }

                            if ("".equals(streetNumber, ignoreCase = true) && addressTypes.contains(
                                    "street_number"
                                )
                            ) {
                                streetNumber = iObj.getString("long_name")
                                if (!"".equals(
                                        streetNumber,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(
                                        streetNumber
                                    )
                                ) {
                                    selectedAddressComponentsArr.add(streetNumber)
                                }
                            }
                            if ("".equals(
                                    route,
                                    ignoreCase = true
                                ) && addressTypes.contains("route")
                            ) {
                                route = iObj.getString("long_name")
                                if (!"".equals(
                                        route,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(route)
                                ) {
                                    selectedAddressComponentsArr.add(route)
                                }
                            }
                            if ("".equals(subLocality2, ignoreCase = true) && addressTypes.contains(
                                    "sublocality_level_2"
                                )
                            ) {
                                subLocality2 = iObj.getString("long_name")
                                if (!"".equals(
                                        subLocality2,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(
                                        subLocality2
                                    )
                                ) {
                                    selectedAddressComponentsArr.add(subLocality2)
                                }
                            }
                            if ("".equals(subLocality1, ignoreCase = true) && addressTypes.contains(
                                    "sublocality_level_1"
                                )
                            ) {
                                subLocality1 = iObj.getString("long_name")
                                if (!"".equals(
                                        subLocality1,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(
                                        subLocality1
                                    )
                                ) {
                                    selectedAddressComponentsArr.add(subLocality1)
                                }
                            }
                            if ("".equals(
                                    locality,
                                    ignoreCase = true
                                ) && addressTypes.contains("locality")
                            ) {
                                locality = iObj.getString("long_name")
                                if (!"".equals(
                                        locality,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(locality)
                                ) {
                                    selectedAddressComponentsArr.add(locality)
                                }
                            }
                            if ("".equals(
                                    administrativeArea2,
                                    ignoreCase = true
                                ) && addressTypes.contains("administrative_area_level_2")
                            ) {
                                administrativeArea2 = iObj.getString("long_name")
                                if (!"".equals(
                                        administrativeArea2,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(
                                        administrativeArea2
                                    )
                                ) {
                                    selectedAddressComponentsArr.add(administrativeArea2)
                                }
                            }
                            if ("".equals(
                                    administrativeArea1,
                                    ignoreCase = true
                                ) && addressTypes.contains("administrative_area_level_1")
                            ) {
                                administrativeArea1 = iObj.getString("long_name")
                                if (!"".equals(
                                        administrativeArea1,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(
                                        administrativeArea1
                                    )
                                ) {
                                    selectedAddressComponentsArr.add(administrativeArea1)
                                }
                            }
                            if ("".equals(
                                    country,
                                    ignoreCase = true
                                ) && addressTypes.contains("country")
                            ) {
                                country = iObj.getString("long_name")
                                if (!"".equals(
                                        country,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(country)
                                ) {
                                    selectedAddressComponentsArr.add(country)
                                }
                            }
                            if ("".equals(
                                    postalCode,
                                    ignoreCase = true
                                ) && addressTypes.contains("postal_code")
                            ) {
                                postalCode = iObj.getString("long_name")
                                if (!"".equals(
                                        postalCode,
                                        ignoreCase = true
                                    ) && !selectedAddressComponentsArr.toString().contains(
                                        postalCode
                                    )
                                ) {
                                    selectedAddressComponentsArr.add(postalCode)
                                }
                            }
                        }

                        fullAddress = StringBuilder()
                        if (selectedAddressComponentsArr.size > 0) {
                            for (i in selectedAddressComponentsArr.indices) {
                                if (i < selectedAddressComponentsArr.size - 1) {
                                    fullAddress.append(selectedAddressComponentsArr[i]).append(", ")
                                } else {
                                    fullAddress.append(selectedAddressComponentsArr[i])
                                }
                            }
                        } else {
                            fullAddress = StringBuilder(zero.getString("formatted_address"))
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        fullAddress = StringBuilder(zero.getString("formatted_address"))
                    }

                } else {
                    fullAddress = StringBuilder(zero.getString("formatted_address"))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return fullAddress.toString()
    }

}
