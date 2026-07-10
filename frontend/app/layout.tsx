import type { Metadata } from "next";
import { Cal_Sans, Geist, Geist_Mono } from "next/font/google";
import "./globals.css";


import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

const calSans = Cal_Sans({
  variable: "--font-cal-sans",
  subsets: ["latin"],
  weight: ["400"],
});

const scriptAcessibilidade = `
(() => {
  if (window.__mapaSocialA11yInicializado) return;
  window.__mapaSocialA11yInicializado = true;

  const CHAVE = "mapa-social-acessibilidade";
  const PADRAO = {
    nivelFonte: 0,
    altoContraste: false,
    movimentoReduzido: false,
    linksSublinhados: false,
    fonteLegivel: false
  };

  let preferencias = { ...PADRAO };
  try {
    preferencias = { ...PADRAO, ...JSON.parse(localStorage.getItem(CHAVE) || "{}") };
  } catch {
    localStorage.removeItem(CHAVE);
  }

  const anunciar = (texto) => {
    const area = document.getElementById("a11y-anuncio");
    if (area) area.textContent = texto;
  };

  const aplicar = (salvar = true) => {
    const raiz = document.documentElement;
    const percentual = 100 + preferencias.nivelFonte * 10;
    raiz.style.fontSize = percentual + "%";
    raiz.classList.toggle("a11y-alto-contraste", preferencias.altoContraste);
    raiz.classList.toggle("a11y-movimento-reduzido", preferencias.movimentoReduzido);
    raiz.classList.toggle("a11y-links-sublinhados", preferencias.linksSublinhados);
    raiz.classList.toggle("a11y-fonte-legivel", preferencias.fonteLegivel);

    const valorFonte = document.querySelector("[data-a11y-font-value]");
    if (valorFonte) {
      valorFonte.textContent = percentual + "%";
      valorFonte.setAttribute("aria-label", "Tamanho atual " + percentual + " por cento");
    }

    const diminuir = document.querySelector('[data-a11y-action="font-decrease"]');
    const aumentar = document.querySelector('[data-a11y-action="font-increase"]');
    if (diminuir) diminuir.disabled = preferencias.nivelFonte <= -1;
    if (aumentar) aumentar.disabled = preferencias.nivelFonte >= 3;

    document.querySelectorAll("[data-a11y-toggle]").forEach((controle) => {
      const propriedade = controle.dataset.a11yToggle;
      controle.setAttribute("aria-checked", String(Boolean(preferencias[propriedade])));
    });

    if (salvar) localStorage.setItem(CHAVE, JSON.stringify(preferencias));
  };

  document.addEventListener("click", (evento) => {
    const origem = evento.target instanceof Element ? evento.target : null;
    const controle = origem?.closest("[data-a11y-action], [data-a11y-toggle]");
    if (!controle) return;

    const propriedade = controle.dataset.a11yToggle;
    if (propriedade) {
      preferencias[propriedade] = !preferencias[propriedade];
      aplicar();
      anunciar(controle.textContent.trim() + (preferencias[propriedade] ? " ativado" : " desativado"));
      return;
    }

    const acao = controle.dataset.a11yAction;
    if (acao === "font-increase" || acao === "font-decrease") {
      const direcao = acao === "font-increase" ? 1 : -1;
      preferencias.nivelFonte = Math.max(-1, Math.min(3, preferencias.nivelFonte + direcao));
      aplicar();
      anunciar("Tamanho do texto ajustado para " + (100 + preferencias.nivelFonte * 10) + " por cento");
    } else if (acao === "reset") {
      preferencias = { ...PADRAO };
      aplicar();
      anunciar("Preferências de acessibilidade restauradas");
    } else if (acao === "close") {
      const detalhes = controle.closest("details");
      if (detalhes) detalhes.open = false;
    }
  });

  document.addEventListener("keydown", (evento) => {
    if (evento.key === "Escape") {
      document.querySelectorAll(".a11y-toolbar details[open]").forEach((detalhes) => detalhes.open = false);
    }
  });

  aplicar(false);
})();
`;

export const metadata: Metadata = {
  title: "Mapa Social do Maranhão",
  description: "Plataforma de mapeamento social",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="pt-BR"
      suppressHydrationWarning
      className={`${geistSans.variable} ${geistMono.variable} ${calSans.variable} h-full antialiased`}
    >
      <body className="flex min-h-screen flex-col bg-slate-50">
        <a href="#conteudo-principal" className="fixed left-4 top-4 z-[200] -translate-y-24 rounded-xl bg-[#061F56] px-4 py-3 font-semibold text-white shadow-xl transition focus:translate-y-0">
          Pular para o conteúdo principal
        </a>
        <div id="conteudo-principal" tabIndex={-1} className="flex flex-1 flex-col bg-slate-50">
          {children}
        </div>
        <script
          id="mapa-social-acessibilidade"
          dangerouslySetInnerHTML={{ __html: scriptAcessibilidade }}
        />
      </body>
    </html>
  );
}
