let marcacao_atual = null;
let territorio_atual = null;

window.onload = function () {
    const seletor_municipios = document.getElementById('f-municipios');

    const mapa = L.map('mapa').setView([-4.96, -45.27], 6)
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(mapa);
    seletor_municipios.addEventListener('change', async function () {
        const municipio = seletor_municipios.value;
        if (municipio != "") {
            const coordenadas_municipio = await lat_e_lon_municipio(municipio);
            if (coordenadas_municipio) {
                mapa.setView(coordenadas_municipio, 10);

                if (marcacao_atual) mapa.removeLayer(marcacao_atual);
                if (territorio_atual) mapa.removeLayer(territorio_atual);

                const coordsRaw = await demarcacao_municipio(municipio);

                if (coordsRaw) {
                    const coordsCorrigidas = coordsRaw.map(ponto => [ponto[1], ponto[0]]);
                    territorio_atual = L.polygon(coordsCorrigidas, {
                        color: 'red',
                        weight: 2,
                        fillColor: 'rgb(80, 31, 41)',
                        fillOpacity: 0.5
                    }).addTo(mapa);
                    mapa.fitBounds(territorio_atual.getBounds());
                }
                marcacao_atual = L.marker(coordenadas_municipio).addTo(mapa);
                let territorio_municipio = L.polygon(
                    await demarcacao_municipio(municipio)
                ).addTo(mapa);

            }
        } else {
            mapa.setView([-4.96, -45.27], 6);
            if (marcacao_atual) {
                mapa.removeLayer(marcacao_atual);
            }
        }

    })

}

async function demarcacao_municipio(municipio) {
    try {
        const resposta = await fetch('geojs-21-mun.json')
        const dados = await resposta.json()
        console.log(dados)
        for (valor in dados['features']) {
            const dados_municipio = dados['features'][valor]['properties']
            const nome_municipio = dados_municipio['name']
            if (nome_municipio === municipio) {
                const coordenadas = dados['features'][valor]['geometry']['coordinates'][0]
                return coordenadas
            }
        }
    } catch (erro) {
        console.log(erro)
    }
}

demarcacao_municipio("São Luís")

async function lat_e_lon_municipio(municipio) {
    const API_lat_lon = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(municipio)},Maranhão&format=json&limit=1`;
    try {
        const resposta = await fetch(API_lat_lon);
        const dados = await resposta.json();

        if (dados.length > 0) {
            const lat = parseFloat(dados[0].lat);
            const lon = parseFloat(dados[0].lon);
            return [lat, lon];
        } else {
            console.error("Município não encontrado");
            return null;
        }
    } catch (erro) {
        console.log(erro);
        return null;
    }
}

