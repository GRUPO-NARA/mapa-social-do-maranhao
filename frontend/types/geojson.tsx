export type Properties = {
    id: string
    name: string
    description: string
}

export type Geometry = {
    type: "Polygon"
    coordinates: number[][][]
}

export type Feature = {
    type: "Feature"
    properties: Properties
    geometry: Geometry
}

export type GeoJSONData = {
    type: "FeatureCollection"
    features: Feature[]
}