const URLS = {
  objetos: 'http://localhost:8081/objetos',
  ocorrencias: 'http://localhost:8082/ocorrencias',
  reivindicacoes: 'http://localhost:8083/reivindicacoes',
  chat: 'http://localhost:8090/chat'
};

const historicoChat = [];

// --- HELPER PARA EVITAR ERROS DE PARSE DE JSON/TEXTO ---
async function processarRespostaHttp(resposta) {
  const texto = await resposta.text();
  try {
    return texto ? JSON.parse(texto) : {};
  } catch (e) {
    return { mensagem: texto };
  }
}

// --- NAVEGAÇÃO ENTRE ABAS ---
function mudarAba(nomeAba) {
  document.querySelectorAll('.tela').forEach(el => el.classList.remove('active'));
  document.querySelectorAll('.tab-btn').forEach(el => el.classList.remove('active'));

  document.getElementById(`tela-${nomeAba}`).classList.add('active');
  const botaoAtivo = document.querySelector(`.tab-btn[onclick="mudarAba('${nomeAba}')"]`);
  if (botaoAtivo) botaoAtivo.classList.add('active');

  if (nomeAba === 'dashboard') {
    carregarDashboard();
  }
}

function adicionarMensagemChat(texto, tipo) {
  const mensagens = document.getElementById('chat-messages');
  const linha = document.createElement('div');
  linha.className = `message-row ${tipo}`;
  const bolha = document.createElement('div');
  bolha.className = 'message-bubble';
  bolha.append(document.createTextNode(texto));
  const meta = document.createElement('span');
  meta.className = 'message-meta';
  meta.innerText = new Date().toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
  bolha.append(meta);
  linha.append(bolha);
  mensagens.append(linha);
  requestAnimationFrame(() => {
    mensagens.scrollTo({ top: mensagens.scrollHeight, behavior: 'smooth' });
  });
}

function mostrarDigitando(visivel) {
  const status = document.getElementById('chat-status');
  status.innerText = visivel ? 'digitando...' : 'online';
  status.classList.toggle('typing', visivel);
}

async function enviarMensagem(event) {
  event.preventDefault();
  const input = document.getElementById('chat-input');
  const mensagem = input.value.trim();
  if (!mensagem || input.disabled) return;

  adicionarMensagemChat(mensagem, 'sent');
  input.value = '';
  document.getElementById('chat-suggestions').classList.add('hidden');
  mostrarDigitando(true);
  input.disabled = true;

  try {
    const historicoParaEnviar = historicoChat.slice(-20);
    // Cada envio cria uma requisição independente ao serviço Chat.
    const resposta = await fetch(URLS.chat, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Accept': 'application/json' },
      body: JSON.stringify({ message: mensagem, history: historicoParaEnviar })
    });
    const dados = await processarRespostaHttp(resposta);
    if (!resposta.ok) {
      throw new Error(dados.message || dados.mensagem || 'O assistente está indisponível.');
    }
    const respostaChat = dados.reply || 'Não recebi uma resposta do assistente.';
    historicoChat.push({ role: 'user', content: mensagem });
    historicoChat.push({ role: 'assistant', content: respostaChat });
    adicionarMensagemChat(respostaChat, 'received');
  } catch (erro) {
    adicionarMensagemChat(`Não foi possível conectar ao assistente: ${erro.message}`, 'received error-bubble');
  } finally {
    mostrarDigitando(false);
    input.disabled = false;
    input.focus();
  }
}

function usarSugestao(texto) {
  const input = document.getElementById('chat-input');
  input.value = texto;
  input.focus();
}

// --- TELA 1: DASHBOARD RESILIENTE ---
async function carregarDashboard() {
  setLoading('objetos');
  setLoading('ocorrencias');
  setLoading('reivindicacoes');

  const resultados = await Promise.allSettled([
    fetch(URLS.objetos).then(async res => res.ok ? await processarRespostaHttp(res) : Promise.reject(res.status)),
    fetch(URLS.ocorrencias).then(async res => res.ok ? await processarRespostaHttp(res) : Promise.reject(res.status)),
    fetch(URLS.reivindicacoes).then(async res => res.ok ? await processarRespostaHttp(res) : Promise.reject(res.status))
  ]);

  renderizarObjetos(resultados[0]);
  renderizarOcorrencias(resultados[1]);
  renderizarReivindicacoes(resultados[2]);
}

function setLoading(servico) {
  const statusEl = document.getElementById(`status-${servico}`);
  if (statusEl) {
    statusEl.className = 'status-indicator loading';
    statusEl.innerText = 'Carregando...';
  }
}

function renderizarObjetos(res) {
  const statusEl = document.getElementById('status-objetos');
  const contentEl = document.getElementById('conteudo-objetos');

  if (res.status === 'fulfilled' && Array.isArray(res.value)) {
    statusEl.className = 'status-indicator ok';
    statusEl.innerText = 'Online';
    contentEl.innerHTML = res.value.map(o => `
      <div class="item-card">
        <strong>#${o.id} - ${o.nome}</strong> (${o.categoria})<br/>
        <small>Cor: ${o.cor} | ${o.descricao}</small>
      </div>
    `).join('') || '<p>Nenhum objeto registrado.</p>';
  } else {
    statusEl.className = 'status-indicator error';
    statusEl.innerText = 'Serviço Indisponível (8081)';
    contentEl.innerHTML = '<p class="error-text">Erro ao conectar com Objetos REST.</p>';
  }
}

function renderizarOcorrencias(res) {
  const statusEl = document.getElementById('status-ocorrencias');
  const contentEl = document.getElementById('conteudo-ocorrencias');

  if (res.status === 'fulfilled' && Array.isArray(res.value)) {
    statusEl.className = 'status-indicator ok';
    statusEl.innerText = 'Online';
    contentEl.innerHTML = res.value.map(oc => `
      <div class="item-card">
        <strong>#${oc.id} - ${oc.tipoOcorrencia || oc.tipo}</strong> [${oc.status}]<br/>
        <small>Objeto ID: ${oc.objetoId} | Local: ${oc.localizacao || oc.local}</small>
      </div>
    `).join('') || '<p>Nenhuma ocorrência registrada.</p>';
  } else {
    statusEl.className = 'status-indicator error';
    statusEl.innerText = 'Serviço Indisponível (8082)';
    contentEl.innerHTML = '<p class="error-text">Erro ao conectar com Ocorrências REST.</p>';
  }
}

function renderizarReivindicacoes(res) {
  const statusEl = document.getElementById('status-reivindicacoes');
  const contentEl = document.getElementById('conteudo-reivindicacoes');

  if (res.status === 'fulfilled' && Array.isArray(res.value)) {
    statusEl.className = 'status-indicator ok';
    statusEl.innerText = 'Online';
    contentEl.innerHTML = res.value.map(r => `
      <div class="item-card">
        <strong>#${r.id} - ${r.nomeSolicitante}</strong> [${r.status}]<br/>
        <small>Ocorrência ID: ${r.ocorrenciaId} | Email: ${r.email}</small>
      </div>
    `).join('') || '<p>Nenhuma reivindicação cadastrada.</p>';
  } else {
    statusEl.className = 'status-indicator error';
    statusEl.innerText = 'Serviço Indisponível (8083)';
    contentEl.innerHTML = '<p class="error-text">Erro ao conectar com Reivindicações REST.</p>';
  }
}

// --- TELA 2: PROCESSAR OCORRÊNCIA ---
async function processarOcorrencia(event) {
  event.preventDefault();
  const msgEl = document.getElementById('msg-cadastrar');
  msgEl.className = 'feedback-msg';
  msgEl.innerText = 'Verificando dados...';

  // Objeto
  const nome = document.getElementById('obj-nome').value.trim();
  const categoria = document.getElementById('obj-categoria').value;
  const cor = document.getElementById('obj-cor').value.trim();
  const marca = document.getElementById('obj-marca').value.trim();
  const descricao = document.getElementById('obj-descricao').value.trim();
  const caracteristicas = document.getElementById('obj-caracteristicas').value.trim();

  // Ocorrência
  const tipo = document.getElementById('oc-tipo').value;
  const localizacao = document.getElementById('oc-local').value.trim();
  const dataOcorrencia = document.getElementById('oc-data').value;
  const observacoes = document.getElementById('oc-observacoes').value.trim();

  // Contato
  const contatoNome = document.getElementById('oc-contato-nome').value.trim();
  const contatoTelefone = document.getElementById('oc-contato-tel').value.trim();
  const contatoEmail = document.getElementById('oc-contato-email').value.trim();

  try {
    // 1. Consulta Objetos
    const resObj = await fetch(URLS.objetos);
    if (!resObj.ok) throw new Error('Serviço de Objetos inacessível (8081).');

    const objetos = await processarRespostaHttp(resObj);
    let objetoEncontrado = Array.isArray(objetos) ? objetos.find(o =>
      o.nome && o.nome.toLowerCase() === nome.toLowerCase() &&
      o.cor && o.cor.toLowerCase() === cor.toLowerCase()
    ) : null;

    let objetoId;

    if (objetoEncontrado) {
      objetoId = objetoEncontrado.id;
    } else {
      // 2. Cria Objeto se não existir
      const resNovoObj = await fetch(URLS.objetos, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nome, categoria, cor, marca, descricao, caracteristicas })
      });

      if (!resNovoObj.ok) throw new Error('Falha ao cadastrar o objeto.');
      const novoObj = await processarRespostaHttp(resNovoObj);
      objetoId = novoObj.id;
    }

    if (!objetoId) {
      throw new Error('Não foi possível obter o ID do objeto para vincular a ocorrência.');
    }

    // 3. Cria Ocorrência com os nomes exatos do OcorrenciaDTO
    const resOc = await fetch(URLS.ocorrencias, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        objetoId: objetoId,
        tipoOcorrencia: tipo,
        status: 'ABERTA',
        descricao: descricao,
        localizacao: localizacao,
        dataOcorrencia: dataOcorrencia,
        observacoes: observacoes,
        contatoNome: contatoNome,
        contatoTelefone: contatoTelefone,
        contatoEmail: contatoEmail
      })
    });

    if (!resOc.ok) throw new Error('Falha ao criar ocorrência.');

    msgEl.className = 'feedback-msg sucesso';
    msgEl.innerText = 'Ocorrência e Objeto registrados com sucesso!';
    document.getElementById('form-ocorrencia').reset();

  } catch (err) {
    msgEl.className = 'feedback-msg erro';
    msgEl.innerText = `Erro: ${err.message}`;
  }
}

// --- TELA 3: PROCESSAR REIVINDICAÇÃO ---
async function processarReivindicacao(event) {
  event.preventDefault();
  const msgEl = document.getElementById('msg-reivindicar');
  msgEl.className = 'feedback-msg';
  msgEl.innerText = 'Enviando solicitação...';

  const ocorrenciaId = document.getElementById('rev-ocorrencia-id').value;
  const nomeSolicitante = document.getElementById('rev-nome').value.trim();
  const email = document.getElementById('rev-email').value.trim();
  const comprovacao = document.getElementById('rev-comprovacao').value.trim();

  try {
    const res = await fetch(URLS.reivindicacoes, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ocorrenciaId, nomeSolicitante, email, comprovacao })
    });

    const respostaDados = await processarRespostaHttp(res);

    if (!res.ok) {
      throw new Error(respostaDados.message || respostaDados.mensagem || 'Ocorrência não encontrada ou serviço indisponível.');
    }

    msgEl.className = 'feedback-msg sucesso';
    msgEl.innerText = 'Reivindicação cadastrada com sucesso!';
    document.getElementById('form-reivindicacao').reset();

  } catch (err) {
    msgEl.className = 'feedback-msg erro';
    msgEl.innerText = `Erro: ${err.message}`;
  }
}

document.addEventListener('DOMContentLoaded', () => {
  carregarDashboard();

  const formularioChat = document.getElementById('chat-form');
  if (formularioChat) {
    formularioChat.addEventListener('submit', enviarMensagem);
  }
});