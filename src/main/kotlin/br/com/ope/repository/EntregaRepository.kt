package br.com.ope.repository

import br.com.ope.model.Atividade
import br.com.ope.model.Entrega
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface EntregaRepository : JpaRepository<Entrega, UUID>