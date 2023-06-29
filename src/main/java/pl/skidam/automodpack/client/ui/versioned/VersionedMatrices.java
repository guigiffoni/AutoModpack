/*
 * This file is part of the AutoModpack project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023 Skidam and contributors
 *
 * AutoModpack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AutoModpack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AutoModpack.  If not, see <https://www.gnu.org/licenses/>.
 */

package pl.skidam.automodpack.client.ui.versioned;

//#if MC >= 12000
//$$import net.minecraft.client.MinecraftClient;
//$$import net.minecraft.client.gui.DrawContext;
//$$import net.minecraft.client.render.VertexConsumerProvider;
//#else
import net.minecraft.client.util.math.MatrixStack;
//#endif

public class VersionedMatrices
//#if MC >= 12000
//$$extends DrawContext
//#else
extends MatrixStack
//#endif
{

//#if MC >= 12000
//$$public VersionedMatrices(MinecraftClient client, VertexConsumerProvider.Immediate vertexConsumers) {
//$$    super(client, vertexConsumers);
//$$}
//$$
//$$public void push() {
//$$    getMatrices().push();
//$$}
//$$
//$$public void pop() {
//$$    getMatrices().pop();
//$$}
//$$
//$$public void scale(float x, float y, float z) {
//$$    getMatrices().scale(x, y, z);
//$$}
//#endif
}
