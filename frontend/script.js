let marcacao_atual = null;
let territorio_atual = null;
let dadosGeo = null;

window.onload = async function () {
  const inputMunicipio = document.getElementById('f-municipios');
  const datalistMunicipios = document.getElementById('lista-municipios');

  const mapa = L.map('mapa').setView([-4.96, -45.27], 6);
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap'
  }).addTo(mapa);

  setTimeout(() => mapa.invalidateSize(), 200);

  dadosGeo = await carregarGeoJSON('coordenadas_municipios.json');

  const municipios = extrair_municipios(dadosGeo);
  popular_datalist_municipios(datalistMunicipios, municipios);

  inputMunicipio.addEventListener('change', async function () {
    const municipio = inputMunicipio.value.trim();
    if (!municipio) return;

    const coordsRaw = demarcacao_municipio_no_cache(municipio, dadosGeo);
    if (!coordsRaw) {
      console.error("Município não encontrado no JSON local:", municipio);
      return;
    }

    if (marcacao_atual) mapa.removeLayer(marcacao_atual);
    if (territorio_atual) mapa.removeLayer(territorio_atual);

    const coordsCorrigidas = coordsRaw.map(p => [p[1], p[0]]); 
    territorio_atual = L.polygon(coordsCorrigidas, {
      color: 'red',
      weight: 2,
      fillColor: 'rgb(80, 31, 41)',
      fillOpacity: 0.5
    }).addTo(mapa);

    mapa.fitBounds(territorio_atual.getBounds());

    const coordenadas_municipio = await lat_e_lon_municipio(municipio);
    if (coordenadas_municipio) {
      marcacao_atual = L.marker(coordenadas_municipio).addTo(mapa);
    }
  });
};

async function carregarGeoJSON(caminho) {
  const resposta = await fetch(caminho);
  return await resposta.json();
}

function extrair_municipios(geo) {
  const nomes = geo.features
    .map(f => f?.properties?.name)
    .filter(Boolean);

  return [...new Set(nomes)].sort((a, b) => a.localeCompare(b, 'pt-BR'));
}

function popular_datalist_municipios(datalistEl, municipios) {
  datalistEl.innerHTML = "";
  for (const nome of municipios) {
    const opt = document.createElement("option");
    opt.value = nome;
    datalistEl.appendChild(opt);
  }
}

function demarcacao_municipio_no_cache(municipio, geo) {
  const feature = geo.features.find(f => f?.properties?.name === municipio);
  return feature ? feature.geometry.coordinates[0] : null; 
}

async function lat_e_lon_municipio(municipio) {
  const API_lat_lon = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(municipio)},Maranhão&format=json&limit=1&countrycodes=br`;
  try {
    const resposta = await fetch(API_lat_lon);
    const dados = await resposta.json();

    if (dados.length > 0) {
      const lat = parseFloat(dados[0].lat);
      const lon = parseFloat(dados[0].lon);
      return [lat, lon];
    } else {
      console.error("Município não encontrado (Nominatim):", municipio);
      return null;
    }
  } catch (erro) {
    console.log(erro);
    return null;
  }
}
