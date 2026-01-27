let marcacao_atual = null;
let territorio_atual = null;
let dadosGeo = null;
let mapa = null; 

window.onload = async function () {
  const seletorMunicipios = document.getElementById('f-municipios');
  
  const anos = [2015,2016,2017,2018,2019,2020,2021,2022,2023,2024,2025,2026];
  const selectAnos = new TomSelect('#f-ano', {
    options: anos.map(a => ({ value: String(a), text: String(a) })),
    items: ['2026'],
    create: false,
    allowEmptyOption: true,
    placeholder: "Selecione o ano"
  });

  const temas = [
    { value: 'educacao', text: 'Educação'},
    { value: 'saude', text: 'Saúde'},
    { value: 'assistencia', text: 'Assistência Social'}
  ];

  const selectTemas = new TomSelect('#f-tema', {
    options: temas,
    create: false,
    allowEmptyOption: true,
    placeholder: "Selecione o tema"
  });

  mapa = L.map('mapa').setView([-4.96, -45.27], 6); 
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; OpenStreetMap'
  }).addTo(mapa);

  setTimeout(() => mapa.invalidateSize(), 200);

  dadosGeo = await carregarGeoJSON('coordenadas_municipios.json');

  const municipios = extrair_municipios(dadosGeo);
  const tomSelectMunicipios = new TomSelect(seletorMunicipios, {
    options: municipios.map(m => ({ value: m, text: m })),
    searchField: ['text'],
    placeholder: "Digite ou selecione o município",
    allowEmptyOption: true,
    create: false,
  });

  document.getElementById('botao').addEventListener('click', aplicarFiltros);

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

async function aplicarFiltros() {
  const municipio = document.getElementById('f-municipios').value.trim();
  const tema = document.getElementById('f-tema').value;
  const ano = document.getElementById('f-ano').value;

  if (!municipio) {
    alert("Selecione um município.");
    return;
  }

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

  console.log("Filtros aplicados:");
  console.log("Município:", municipio);
  console.log("Tema:", tema);
  console.log("Ano:", ano);
}
