package qouteall.imm_ptl.core.mixin.common;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import qouteall.imm_ptl.core.IPGlobal;
import qouteall.imm_ptl.core.ducks.IEMinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer implements IEMinecraftServer {
    @Shadow
    public abstract ProfilerFiller getProfiler();
    
    @Shadow
    @Final
    public LevelStorageSource.LevelStorageAccess storageSource;
    
    private boolean portal_areAllWorldsLoaded;
    
    @Inject(
        method = "Lnet/minecraft/server/MinecraftServer;runServer()V",
        at = @At("RETURN")
    )
    private void onServerClose(CallbackInfo ci) {
        IPGlobal.serverCleanupSignal.emit();
    }
    
    @Inject(
        method = "Lnet/minecraft/server/MinecraftServer;createLevels(Lnet/minecraft/server/level/progress/ChunkProgressListener;)V",
        at = @At("RETURN")
    )
    private void onFinishedLoadingAllWorlds(
        CallbackInfo ci
    ) {
        portal_areAllWorldsLoaded = true;
    }
    
    @Override
    public boolean portal_getAreAllWorldsLoaded() {
        return portal_areAllWorldsLoaded;
    }
}
