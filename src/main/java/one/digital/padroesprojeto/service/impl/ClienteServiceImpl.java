package one.digital.padroesprojeto.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digital.padroesprojeto.model.Cliente;
import one.digital.padroesprojeto.model.ClienteRepository;
import one.digital.padroesprojeto.model.Endereco;
import one.digital.padroesprojeto.model.EnderecoRepository;
import one.digital.padroesprojeto.service.ClienteService;
import one.digital.padroesprojeto.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {

    // Singleton: injetar os componentes do spring com @Autowired
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;
    // Strategy: Implementar os metodos definidos na interface
    // Facade: Abstrair integracoes com subsistemas, provendo uma interface simples

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
        
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        
        clienteRepository.save(cliente);
    }

}
