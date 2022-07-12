package com.eleks.academy.whoami.repository;

import com.eleks.academy.whoami.core.SynchronousGame;

import java.util.List;
import java.util.Optional;

public interface GameRepository {

	List<SynchronousGame> findAllAvailable(String player);

	SynchronousGame save(SynchronousGame game);

	Optional<SynchronousGame> findById(String id);

	void deleteByIdPlayer(String id, String player);

}
