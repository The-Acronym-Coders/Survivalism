package com.teamacronymcoders.survivalism.utils.configs.modules;

import com.teamacronymcoders.base.modulesystem.ModuleHandler;
import com.teamacronymcoders.base.modulesystem.dependencies.IDependency;

import javax.annotation.Nullable;

public class OrDependancy implements IDependency {
    private IDependency dep1;
    private IDependency dep2;
    private boolean silent;

    public OrDependancy(IDependency dep1, IDependency dep2) {
        this(dep1, dep2, false);
    }

    public OrDependancy(IDependency dep1, IDependency dep2, boolean silent) {
        this.dep1 = dep1;
        this.dep2 = dep2;
        this.silent = silent;
    }

    @Override
    public boolean isSilent() {
        return this.silent;
    }

    @Override
    public boolean isMet(ModuleHandler moduleHandler) {
        return dep1.isMet(moduleHandler) || dep2.isMet(moduleHandler);
    }

    @Nullable
    @Override
    public String notMetMessage() {
        return dep1.notMetMessage() + dep2.notMetMessage();
    }

}
